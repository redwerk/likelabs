#import "PhotoOverlayController.h"
#import "SettingsDao.h"
#import "MaskedTextFieldDelegate.h"

@interface PhotoOverlayController ()
@property (nonatomic, assign) BOOL textPlaceholderActive;
@property (nonatomic, retain) NSString* phonePrefix;
@property (nonatomic, retain) MaskedTextFieldDelegate* maskedTextFieldDelegate;

- (BOOL) validatePhone;
- (void) layoutSubviewsForInterfaceOrientation:(UIInterfaceOrientation)orientation;
- (void) savePhone;
@end

@implementation PhotoOverlayController
NSString *const kPrimaryPhoneDidCancel = @"PrimaryPhoneDidCancel";
NSString *const kPrimaryPhoneDone = @"PrimaryPhoneDone";
int cursorPos;


@synthesize textField = _textField;
@synthesize buttonsView = _buttonsView;
@synthesize phone = _phone;
@synthesize textPlaceholderActive = _textPlaceholderActive;
@synthesize phonePrefix = _phonePrefix;
@synthesize maskedTextFieldDelegate = _maskedTextFieldDelegate;

#pragma mark - Initialization
- (id) initWithPhone:(NSString*) phone {
    if (self = [super init]) {
        self.phone = phone;
        SettingsDao* dao = [[SettingsDao alloc] init];
        self.phonePrefix = dao.phonePrefix;
        [dao release];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.view.frame = [UIScreen mainScreen].bounds;
    self.view.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.7];
    
    NSMutableString* fullMask = [NSMutableString stringWithString:self.phonePrefix];
    [fullMask appendString:PHONE_FORMAT];
    
    self.textField.delegate =  self.maskedTextFieldDelegate = [[[MaskedTextFieldDelegate alloc] initWithMask:fullMask maskCharacter:PHONE_DIGIT_MASK andOuterDelegate:self] autorelease];
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];    
    if (self.phone && self.phone.length) {
        self.textField.text = self.phone;
        self.textPlaceholderActive = NO;
    } else {
        self.textField.text = fullMask;
        self.textPlaceholderActive = YES;
    }
    [self.textField becomeFirstResponder];    
}

- (void)viewDidAppear:(BOOL)animated
{
    if (!self.phone || !self.phone.length) {
        UITextPosition* newPosition = [self.textField positionFromPosition:self.textField.beginningOfDocument offset:[self.maskedTextFieldDelegate getFirstPlaceholerPositionInMask]];
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
    [self setMaskedTextFieldDelegate:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    [_textField release];
    [_buttonsView release];
    [_phone release];
    [_phonePrefix release];
    [_maskedTextFieldDelegate release];
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

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    BOOL phoneValid = [self validatePhone];
    if (phoneValid){
        [self savePhone];
    }
    return phoneValid;
}

- (BOOL) validatePhone {
    if([self.textField.text rangeOfString:PHONE_DIGIT_MASK].location != NSNotFound) {
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Invalid phone format" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
        return NO;
    } 
    return YES;
}

- (void) savePhone {
    self.phone = self.textField.text;
    [[NSNotificationCenter defaultCenter] postNotificationName:kPrimaryPhoneDone object:nil];    
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
