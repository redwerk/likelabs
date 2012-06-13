#import <AVFoundation/AVFoundation.h>
#import "PhotoRecipientsOverlayController.h"
#import "SettingsDao.h"
#import "MaskedTextFieldDelegate.h"

@interface PhotoRecipientsOverlayController ()
@property (nonatomic, assign) ContactType contactType;
@property (nonatomic, assign) BOOL textPlaceholderActive;
@property (nonatomic, retain) MaskedTextFieldDelegate* maskedTextFieldDelegate;

@end

@implementation PhotoRecipientsOverlayController
NSString *const kRecipientsDidCancel = @"RecipientsDidCancel";
NSString *const kRecipientsDone = @"RecipientsDone";
NSUInteger const BTN_TAG_OFFSET = 50;
int cursorPos;

@synthesize recipientsView;
@synthesize recipientContactField;
@synthesize recipients = _recipients;
@synthesize contactType = _contactType;
@synthesize textPlaceholderActive = _textPlaceholerActive;
@synthesize maskedTextFieldDelegate = _maskedTextFieldDelegate;

#pragma mark - Initialization

- (id) initWithContactType:(ContactType)contactType andRecipients:(NSMutableArray*) recipients {
    if (self = [super init]) {
        self.recipients = recipients;
        self.contactType = contactType;
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
        SettingsDao *dao = [[SettingsDao alloc] init];
        NSMutableString *fullMask = [NSMutableString stringWithString:dao.phonePrefix];
        [dao release];
        [fullMask appendString:PHONE_FORMAT];
        self.recipientContactField.placeholder = self.recipientContactField.text = fullMask;
        self.recipientContactField.keyboardType = UIKeyboardTypePhonePad;
        self.recipientContactField.textColor = [UIColor blackColor];
        self.recipientContactField.delegate = self.maskedTextFieldDelegate = [[[MaskedTextFieldDelegate alloc] initWithMask:fullMask maskCharacter:PHONE_DIGIT_MASK andOuterDelegate:self] autorelease];
    } else {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textFieldDidChange) name:UITextFieldTextDidChangeNotification object:nil];
        self.recipientContactField.placeholder = self.recipientContactField.text = @"my@email.com";    
        self.recipientContactField.keyboardType = UIKeyboardTypeEmailAddress;
        self.recipientContactField.textColor = [UIColor grayColor];
    }
    self.textPlaceholderActive = YES;
    
    [self layoutRecipients];
    [self.recipientContactField becomeFirstResponder];
}

- (void)viewDidAppear:(BOOL)animated
{
    if (self.contactType == ContactTypePhone) {
        UITextPosition* newPosition = [self.recipientContactField positionFromPosition:self.recipientContactField.beginningOfDocument offset:[self.maskedTextFieldDelegate getFirstPlaceholerPositionInMask]];
        self.recipientContactField.selectedTextRange = [self.recipientContactField textRangeFromPosition:newPosition toPosition:newPosition];
    }
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setRecipientsView:nil];
    [self setRecipientContactField:nil];
    [self setRecipients:nil];
    [self setMaskedTextFieldDelegate:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    [recipientsView release];
    [recipientContactField release];
    [_recipients release];
    [_maskedTextFieldDelegate release];
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
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    BOOL conactValid = (self.contactType == ContactTypeEmail) ? [self validateEmail] : [self validatePhone];
    if (conactValid) {
        [self saveConact];
    }
    return conactValid;
}

- (BOOL) validatePhone {
    if ([self.recipientContactField.text rangeOfString:PHONE_DIGIT_MASK].location != NSNotFound) {
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Invalid phone format." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
        return NO;
    } 
    return YES;
}

- (BOOL) validateEmail {
    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:@"^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@(([0-9a-zA-Z])+([-\\w]*[0-9a-zA-Z])*\\.)+[a-zA-Z]{2,9})$" options:NSRegularExpressionCaseInsensitive error:nil];
    if (self.textPlaceholderActive || ![regExp numberOfMatchesInString:self.recipientContactField.text options:0 range:NSMakeRange(0, self.recipientContactField.text.length)]){
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Invalid email format." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
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
