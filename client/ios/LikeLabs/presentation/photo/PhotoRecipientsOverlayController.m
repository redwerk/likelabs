#import <AVFoundation/AVFoundation.h>
#import "PhotoRecipientsOverlayController.h"

@interface PhotoRecipientsOverlayController ()
@property (nonatomic, assign) ContactType contactType;
@property (nonatomic, assign) BOOL textPlaceholderActive;

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string;
- (BOOL)isNumber:(NSString*)string;
- (NSRange)convertFromTextFieldToPhoneRange:(NSRange) range;
- (NSRange)convertFromPhoneToTextFieldRange:(NSRange) range;
- (void) layoutRecipients;
- (BOOL) validatePhone;
- (BOOL) validateEmail;
- (void) saveConact;
@end

@implementation PhotoRecipientsOverlayController
NSString *const kRecipientsDidCancel = @"RecipientsDidCancel";
NSString *const kRecipientsDone = @"RecipientsDone";
NSUInteger const BTN_TAG_OFFSET = 50;
static NSString *const PHONE_FORMAT = @"+38(___)___-____";
static NSString *const PHONE_DIGIT_MASK = @"_";
static NSString *const PHONE_VALIDATION_PATTERN = @"^\\+38\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}$";
int cursorPos;

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
        self.recipientContactField.placeholder = self.recipientContactField.text =  PHONE_FORMAT;
        self.recipientContactField.keyboardType = UIKeyboardTypePhonePad;
        self.recipientContactField.textColor = [UIColor blackColor];
    } else {
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
        UITextPosition* newPosition = [self.recipientContactField positionFromPosition:self.recipientContactField.beginningOfDocument offset:[self getFirstPlaceholerPositionInMask]];
        self.recipientContactField.selectedTextRange = [self.recipientContactField textRangeFromPosition:newPosition toPosition:newPosition];
    }
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
    
    if (self.contactType == ContactTypePhone) {
        NSString* phone = textField.text;
        
        NSString* number = [self maskPhoneNumber:phone inRange:range insertString:string];
        
        textField.text = number;
        NSRange currentCursorPos = [self convertFromPhoneToTextFieldRange:NSMakeRange(cursorPos, 1)];
        UITextPosition* newPosition = [textField positionFromPosition:textField.beginningOfDocument offset:currentCursorPos.location];
        textField.selectedTextRange = [textField textRangeFromPosition:newPosition toPosition:newPosition];
        
        return NO;
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
    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:PHONE_VALIDATION_PATTERN options:NSRegularExpressionCaseInsensitive error:nil];
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

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string
{
    if (phone.length == 0)
    {
        phone = [NSString stringWithString:PHONE_FORMAT];
        range = NSMakeRange([self getFirstPlaceholerPositionInMask], 1);
    }
    
    phone = [self getPhoneDigitsFromString:phone];
    
    cursorPos = 0;
    NSMutableString* phoneNumber = [NSMutableString stringWithString:phone];
    cursorPos = [self convertFromTextFieldToPhoneRange:range].location;
    if (cursorPos > phone.length)
        cursorPos = phone.length;
    
    if (string.length == 1)
    {
        if ([self isNumber:string]) {
            NSUInteger i = (cursorPos >= phoneNumber.length) ? phoneNumber.length : cursorPos;
            [phoneNumber insertString:string atIndex:i];
            cursorPos = i;
        } else {
            cursorPos--;
        }
    }
    else
    {
        if (![self charIsMask:[PHONE_FORMAT characterAtIndex:range.location]])
            cursorPos--;
        if (range.location > [self getFirstPlaceholerPositionInMask] - 1) {
            NSUInteger i = (cursorPos >= phone.length) ? phone.length - 1 : cursorPos;
            [phoneNumber deleteCharactersInRange:NSMakeRange(i, 1)];
            cursorPos = (cursorPos >= phone.length) ? i - 1 : cursorPos - 1;
        }
    }
    
    phone = phoneNumber;
    
    NSMutableString* number = [NSMutableString stringWithString:PHONE_FORMAT];
    
    for (int i = [self getStartingDigitsCountInMask]; i < phone.length; i++)
    {
        NSRange nextNumber = [number rangeOfString:PHONE_DIGIT_MASK];
        if (nextNumber.location == NSNotFound)
        {
            break;
        }
        
        [number replaceCharactersInRange:nextNumber withString:[NSString stringWithFormat:@"%c", [phone characterAtIndex:i]]];
    }
    
    return number;
}

- (NSRange)convertFromTextFieldToPhoneRange:(NSRange) range
{
    int pos = 0;
    for (int i = 0; i < range.location; i++)
    {
        if ([self charIsMask:[PHONE_FORMAT characterAtIndex:i]])
            pos++;
    }
    pos += [self getStartingDigitsCountInMask];
    return NSMakeRange(pos, 1);
}

- (NSRange)convertFromPhoneToTextFieldRange:(NSRange) range
{
    if (range.location == 0 || range.location == NSNotFound || range.location == NSMakeRange(-1, 1).location)
        return NSMakeRange([self getFirstPlaceholerPositionInMask],1);
    
    int pos = 0;
    int phonePos = range.location - [self getStartingDigitsCountInMask]+1;
    while (phonePos && pos < PHONE_FORMAT.length) {
        if ([self charIsMask:[PHONE_FORMAT characterAtIndex:pos]])
            phonePos--;
        pos++;
    }
    return NSMakeRange(pos ? pos : [self getFirstPlaceholerPositionInMask], 1);
}

- (BOOL)isNumber:(NSString*)string
{
    for (int i = 0; i < string.length; i++)
    {
        if (!isdigit([string characterAtIndex:i]))
            return NO;
    }
    return YES;
}

- (NSUInteger) getFirstPlaceholerPositionInMask {
    return [PHONE_FORMAT rangeOfString:PHONE_DIGIT_MASK].location;
}

- (NSUInteger) getStartingDigitsCountInMask { //in 8(___)___-____ returns 1; in +38(___)___-____ returns 2;
    int pos = 0;
    for (int i = 0; i < PHONE_FORMAT.length; i++)
    {
        if (isdigit([PHONE_FORMAT characterAtIndex:i])) {
            pos++;
        } else if ([self charIsMask:[PHONE_FORMAT characterAtIndex:i]]){
            break;
        }
    }
    return pos;
}

- (NSString*) getPhoneDigitsFromString: (NSString*) string {
    NSRegularExpression* regex = [NSRegularExpression regularExpressionWithPattern:@"\\D" options:NSRegularExpressionCaseInsensitive error:nil];
    return [regex stringByReplacingMatchesInString:string options:0 range:NSMakeRange(0, string.length) withTemplate:@""];
}

- (BOOL) charIsMask:(unichar)unichar {
    return [[NSString stringWithFormat:@"%c", unichar] isEqualToString:PHONE_DIGIT_MASK];
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
