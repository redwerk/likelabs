#import <AVFoundation/AVFoundation.h>
#import "PhotoRecipientsOverlayController.h"

@interface PhotoRecipientsOverlayController ()
@property (nonatomic, assign) ContactType contactType;
@property (nonatomic, assign) BOOL textPlaceholderActive;
@end

@implementation PhotoRecipientsOverlayController
NSString *const kRecipientsDidCancel = @"RecipientsDidCancel";
NSString *const kRecipientsDone = @"RecipientsDone";
NSUInteger const BTN_TAG_OFFSET = 50;
NSString *const phoneRecipientsFormat = @"8(___)___-____";
NSString *const phoneRecipientsDigitMask = @"_";
int myPhoneSemaphore;

@synthesize recipientsView;
@synthesize recipientContactField;
@synthesize recipients = _recipients;
@synthesize contactType = _contactType;
@synthesize textPlaceholderActive = _textPlaceholerActive;

#pragma mark - Initialization

- (id) initWithContactType:(ContactType)contactType andRecipients:(NSMutableArray*) recipients {
    if (self = [super init]) {
        self.recipients = recipients;
        self.contactType = contactType;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textFieldDidChange) name:UITextFieldTextDidChangeNotification object:nil];
        myPhoneSemaphore = 1;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.view.frame = [UIScreen mainScreen].bounds;
    self.recipientContactField.frame = CGRectMake(0, 0, 377, 75);  
    self.view.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.7];
    if (self.contactType == ContactTypePhone) {
        self.recipientContactField.placeholder = self.recipientContactField.text =  @"8(XXX)XXX-XXXX";
        self.recipientContactField.keyboardType = UIKeyboardTypePhonePad;
    } else {
        self.recipientContactField.placeholder = self.recipientContactField.text = @"my@email.com";    
        self.recipientContactField.keyboardType = UIKeyboardTypeEmailAddress;
    }
    self.textPlaceholderActive = YES;
    self.recipientContactField.textColor = [UIColor grayColor];
    [self layoutRecipients];
    [self.recipientContactField becomeFirstResponder];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setRecipientsView:nil];
    [self setRecipientContactField:nil];
    [self setRecipients:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [recipientsView release];
    [recipientContactField release];
    [_recipients release];
    [super dealloc];
}

#pragma mark - Recipients management

- (void) layoutRecipients {
    for (UIView* view in self.recipientsView.subviews) {
        if ([view respondsToSelector:@selector(removeFromSuperview)]) {
            [view removeFromSuperview];
        }
    }
    CGFloat labelHeight = 50;
    CGFloat padding = 10;
    for (NSUInteger i = 0; i<self.recipients.count; i++) {
        UILabel* label = [[UILabel alloc] initWithFrame:CGRectMake(0, (labelHeight + padding)*i, 350, labelHeight)];
        label.tag = i+1;
        label.font = [UIFont systemFontOfSize:32];
        label.textColor = [UIColor whiteColor];
        label.backgroundColor = [UIColor clearColor];
        Contact* contact = [self.recipients objectAtIndex:i];
        label.text = contact.contactString;        
        [self.recipientsView addSubview:label];
        [label release];
        
        UIButton* delBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        delBtn.tag = BTN_TAG_OFFSET + i + 1;
        UIImage* delImg = [UIImage imageNamed:@"delete_btn.png"];
        [delBtn setBackgroundImage:delImg forState:UIControlStateNormal];
        delBtn.frame = CGRectMake(350, (labelHeight + padding)*i, delImg.size.width, delImg.size.height);
        [delBtn addTarget:self action:@selector(deleteRecipient:) forControlEvents:UIControlEventTouchDown];        
        [self.recipientsView addSubview:delBtn];
    }
}

- (void) deleteRecipient: (UIButton *) sender {
    [self.recipients removeObjectAtIndex: sender.tag - BTN_TAG_OFFSET -1];
    [self layoutRecipients];
}

#pragma mark - TextField delegate

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (self.textPlaceholderActive) {
        if (string.length == 0) return NO;
        self.recipientContactField.text = @"";
        self.recipientContactField.textColor = [UIColor blackColor];
        self.textPlaceholderActive = NO;
    } 
    return YES;
}

- (void)textFieldDidChange {
    if (!self.textPlaceholderActive && self.recipientContactField.text.length == 0) {
        self.recipientContactField.textColor = [UIColor grayColor];
        self.recipientContactField.text = self.recipientContactField.placeholder;
        self.textPlaceholderActive = YES;
    }
    
    if (self.contactType == ContactTypePhone) {
        NSString* res = [self maskPhoneNumber:self.recipientContactField.text];
        if (res == @"")
        {
            myPhoneSemaphore = 0;
            return;
        }
        
        self.recipientContactField.text = res;
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    BOOL conactValid = (self.contactType == ContactTypeEmail) ? [self validateEmail] : [self validatePhone];
    if (conactValid) {
        [self saveConact];
    }
    return conactValid;
}

- (BOOL) validatePhone {
    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:@"^8\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}$" options:NSRegularExpressionCaseInsensitive error:nil];
    if (![regExp numberOfMatchesInString:self.recipientContactField.text options:0 range:NSMakeRange(0, self.recipientContactField.text.length)]){
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Phone should be in format 8(XXX)XXX-XXXX" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
        return NO;
    } 
    return YES;
}

- (BOOL) validateEmail {
    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:@"^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@(([0-9a-zA-Z])+([-\\w]*[0-9a-zA-Z])*\\.)+[a-zA-Z]{2,9})$" options:NSRegularExpressionCaseInsensitive error:nil];
    if (self.textPlaceholderActive || ![regExp numberOfMatchesInString:self.recipientContactField.text options:0 range:NSMakeRange(0, self.recipientContactField.text.length)]){
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Email should be in format my@email.com" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
        return NO;
    } 
    return YES;    
}

- (void) saveConact {
    Contact* contact = [[Contact alloc] initWithContactType:self.contactType andContactString:self.recipientContactField.text];
    [self.recipients addObject:contact];
    [contact release];
    [[NSNotificationCenter defaultCenter] postNotificationName:kRecipientsDone object:nil];
}

- (NSString*) maskPhoneNumber:(NSString*) phone{
    static int deleteIndex = 0;
    
    if (myPhoneSemaphore) return @"";
    myPhoneSemaphore = 1;
    
    NSString* input = [NSString stringWithString:phone];
    if (phone.length == 1) 
        input = phoneRecipientsFormat;
    
    if (input.length < phoneRecipientsFormat.length)
    {
        NSMutableString* afterDelete = [NSMutableString stringWithString:[NSString stringWithFormat:@"%@%@", input, phoneRecipientsDigitMask]];
        if (deleteIndex > 1)
        {
            [afterDelete replaceCharactersInRange:NSMakeRange(deleteIndex, phoneRecipientsFormat.length-deleteIndex) withString:[phoneRecipientsFormat substringFromIndex:deleteIndex]];
            
            deleteIndex--;
            
            if (deleteIndex > 1)
            {
                while (!isdigit([afterDelete characterAtIndex:deleteIndex]))
                    deleteIndex--;
            }
        }
        
        return afterDelete;
    }
    
    NSRange nextNumber = [input rangeOfString:phoneRecipientsDigitMask];
    if (nextNumber.location != NSNotFound)
        deleteIndex = nextNumber.location;
    
    char lastInputChar = [phone characterAtIndex:[phone length] - 1];
    
    NSMutableString* number = [NSMutableString stringWithString:input];
    if (number.length > phoneRecipientsFormat.length)
        [number replaceCharactersInRange:NSMakeRange(phoneRecipientsFormat.length, 1) withString:@""];
    
    if (nextNumber.location == NSNotFound)
    {
        myPhoneSemaphore = 1;
        return number;
    }
    if (isdigit(lastInputChar))
        [number replaceCharactersInRange:nextNumber withString:[NSString stringWithFormat:@"%c", lastInputChar]];
    
    
    return number;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

#pragma mark - Actions

- (IBAction)cancel:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:kRecipientsDidCancel object:nil];
}

- (IBAction)done:(id)sender {
    BOOL contactValid = (self.contactType == ContactTypeEmail) ? [self validateEmail] : [self validatePhone];
    if (contactValid) {
        [self saveConact];        
    }
}
@end
