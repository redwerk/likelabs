#import "PhotoOverlayController.h"
#import "SettingsDao.h"

@interface PhotoOverlayController ()
@property (nonatomic, assign) BOOL textPlaceholderActive;
@property (nonatomic, retain) NSString* phonePrefix;

- (BOOL) validatePhone;
- (void) layoutSubviewsForInterfaceOrientation:(UIInterfaceOrientation)orientation;
- (void) savePhone;

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string;
- (BOOL)isNumber:(NSString*)string;
- (NSRange)convertFromTextFieldToPhoneRange:(NSRange) range;
- (NSRange)convertFromPhoneToTextFieldRange:(NSRange) range;
- (NSUInteger) getFirstPlaceholerPositionInMask;
@end

@implementation PhotoOverlayController
NSString *const kPrimaryPhoneDidCancel = @"PrimaryPhoneDidCancel";
NSString *const kPrimaryPhoneDone = @"PrimaryPhoneDone";
static NSString *const PHONE_FORMAT = @"(___)___-____";
static NSString *const PHONE_DIGIT_MASK = @"_";
//static NSString *const PHONE_VALIDATION_PATTERN = @"^\\+38\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}$";
int cursorPos;


@synthesize textField = _textField;
@synthesize buttonsView = _buttonsView;
@synthesize phone = _phone;
@synthesize textPlaceholderActive = _textPlaceholderActive;
@synthesize phonePrefix = _phonePrefix;

#pragma mark - Initialization
- (id) initWithPhone:(NSString*) phone {
    if (self = [super init]) {
        self.phone = phone;
        SettingsDao* dao = [[SettingsDao alloc] init];
        self.phonePrefix = dao.phonePrefix;
        [dao release];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textFieldDidChange) name:UITextFieldTextDidChangeNotification object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.view.frame = [UIScreen mainScreen].bounds;
    self.view.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.7];
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
    if (self.phone && self.phone.length) {
        NSMutableString *phone = [NSMutableString stringWithString:self.phone];
        [phone deleteCharactersInRange:NSMakeRange(0, self.phonePrefix.length)];
        self.textField.text = phone;
        self.textPlaceholderActive = NO;
    } else {
        self.textField.text = PHONE_FORMAT;
        self.textPlaceholderActive = YES;
    }
    [self.textField becomeFirstResponder];    
}

- (void)viewDidAppear:(BOOL)animated
{
    if (!self.phone || !self.phone.length) {
        UITextPosition* newPosition = [self.textField positionFromPosition:self.textField.beginningOfDocument offset:[self getFirstPlaceholerPositionInMask]];
        self.textField.selectedTextRange = [self.textField textRangeFromPosition:newPosition toPosition:newPosition];
    }
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setTextField:nil];
    [self setButtonsView:nil];
    [self setPhone:nil];
    [self setPhonePrefix:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    [_textField release];
    [_buttonsView release];
    [_phone release];
    [_phonePrefix release];
    [super dealloc];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
}

- (void) layoutSubviewsForInterfaceOrientation:(UIInterfaceOrientation)orientation {
    if (UIInterfaceOrientationIsLandscape(orientation)) {
        //LANDSCAPE
        self.textField.frame = CGRectMake(152, 244, 720, 78);
        self.buttonsView.frame = CGRectMake(566, 335, 306, 75);
    } else {
        //PORTRAIT
        self.textField.frame = CGRectMake(32, 323, 700, 78);
        self.buttonsView.frame = CGRectMake(424, 422, 306, 75);
    }
}

#pragma mark - TextField delegate

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (self.textPlaceholderActive) {
        if (string.length == 0) return NO;
        self.textField.text = @"";
        self.textPlaceholderActive = NO;
    } 
    NSString* phone = textField.text;
    
    NSString* number = [self maskPhoneNumber:phone inRange:range insertString:string];
    
    textField.text = number;
    NSRange currentCursorPos = [self convertFromPhoneToTextFieldRange:NSMakeRange(cursorPos, 1)];
    UITextPosition* newPosition = [textField positionFromPosition:textField.beginningOfDocument offset:currentCursorPos.location];
    textField.selectedTextRange = [textField textRangeFromPosition:newPosition toPosition:newPosition];
    
    return NO;
}

- (void)textFieldDidChange {
    if (!self.textPlaceholderActive && self.textField.text.length == 0) {
        self.textField.text = self.textField.placeholder;
        self.textPlaceholderActive = YES;
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    BOOL phoneValid = [self validatePhone];
    if (phoneValid){
        [self savePhone];
    }
    return phoneValid;
}

- (BOOL) validatePhone {
//    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:PHONE_VALIDATION_PATTERN options:NSRegularExpressionCaseInsensitive error:nil];
//    if (![regExp numberOfMatchesInString:self.textField.text options:0 range:NSMakeRange(0, self.textField.text.length)]){
    if([self.textField.text rangeOfString:PHONE_DIGIT_MASK].location != NSNotFound) {
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Invalid phone format" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
        return NO;
    } 
    return YES;
}

- (void) savePhone {
    NSMutableString *phone = [NSMutableString stringWithString:self.phonePrefix];
    [phone appendString:self.textField.text];
    self.phone = phone;
    [[NSNotificationCenter defaultCenter] postNotificationName:kPrimaryPhoneDone object:nil];    
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
    if (range.location == 0) {
        return NSMakeRange(2, 1);
    }
    if (range.location == NSNotFound || range.location == NSMakeRange(-1, 1).location) {
        return NSMakeRange([self getFirstPlaceholerPositionInMask],1);
    }
    
    
    int pos = 0;
    int phonePos = range.location - [self getStartingDigitsCountInMask]+1;
    while (phonePos && pos < PHONE_FORMAT.length) {
        if ([self charIsMask:[PHONE_FORMAT characterAtIndex:pos]])
            phonePos--;
        pos++;
    }
    return NSMakeRange(pos/* ? pos : [self getFirstPlaceholerPositionInMask]*/, 1);
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

#pragma mark - Actions

- (IBAction)cancel:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:kPrimaryPhoneDidCancel object:nil];
}

- (IBAction)done:(id)sender {
    if ([self validatePhone]) {
        [self savePhone];
    }
}
@end
