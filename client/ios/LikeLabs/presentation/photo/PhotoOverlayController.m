#import "PhotoOverlayController.h"

@interface PhotoOverlayController ()
@property (nonatomic, assign) BOOL textPlaceholderActive;

- (BOOL) validatePhone;
- (void) layoutSubviewsForInterfaceOrientation:(UIInterfaceOrientation)orientation;
- (void) savePhone;
@end

@implementation PhotoOverlayController
NSString *const kPrimaryPhoneDidCancel = @"PrimaryPhoneDidCancel";
NSString *const kPrimaryPhoneDone = @"PrimaryPhoneDone";
NSString *const phoneFormat = @"8(___)___-____";
NSString *const phoneDigitMask = @"_";
int myPhoneSemaphore;


@synthesize textField = _textField;
@synthesize buttonsView = _buttonsView;
@synthesize phone = _phone;
@synthesize textPlaceholderActive = _textPlaceholderActive;

#pragma mark - Initialization
- (id) initWithPhone:(NSString*) phone {
    if (self = [super init]) {
        self.phone = phone;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textFieldDidChange) name:UITextFieldTextDidChangeNotification object:nil];
        myPhoneSemaphore = 1;
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
        self.textField.text = self.phone;
        self.textField.textColor = [UIColor blackColor];
        self.textPlaceholderActive = NO;
    } else {
        self.textField.text = self.textField.placeholder;
        self.textField.textColor = [UIColor grayColor];
        self.textPlaceholderActive = YES;
    }
    [self.textField becomeFirstResponder];    
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setTextField:nil];
    [self setButtonsView:nil];
    [self setPhone:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    [_textField release];
    [_buttonsView release];
    [_phone release];
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
   // NSLog(@"%@",textField.selectedTextRange.start);
    //UITextRange* textRange = [[[UITextRange alloc] init] autorelease];
    if (self.textPlaceholderActive) {
        if (string.length == 0) return NO;
        self.textField.text = @"";
        self.textField.textColor = [UIColor blackColor];
        self.textPlaceholderActive = NO;
    } 
    return YES;
}

- (void)textFieldDidChange {
    if (!self.textPlaceholderActive && self.textField.text.length == 0) {
        self.textField.textColor = [UIColor grayColor];
        self.textField.text = self.textField.placeholder;
        self.textPlaceholderActive = YES;
    }
    
    NSString* res = [self maskPhoneNumber:self.textField.text];
    if (res == @"")
    {
        myPhoneSemaphore = 0;
        return;
    }
    
    self.textField.text = res;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    BOOL phoneValid = [self validatePhone];
    if (phoneValid){
        [self savePhone];
    }
    return phoneValid;
}

- (BOOL) validatePhone {
    NSRegularExpression* regExp = [NSRegularExpression regularExpressionWithPattern:@"^8\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}$" options:NSRegularExpressionCaseInsensitive error:nil];
    if (![regExp numberOfMatchesInString:self.textField.text options:0 range:NSMakeRange(0, self.textField.text.length)]){
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:@"Phone should be in format 8(XXX)XXX-XXXX" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
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

- (NSString*) maskPhoneNumber:(NSString*) phone
{
    static int deleteIndex = 0;
    
    if (myPhoneSemaphore) return @"";
    myPhoneSemaphore = 1;
    
    NSString* input = [NSString stringWithString:phone];
    if (phone.length == 1) 
        input = phoneFormat;
    
    if (input.length < phoneFormat.length)
    {
        NSMutableString* afterDelete = [NSMutableString stringWithString:[NSString stringWithFormat:@"%@%@", input, phoneDigitMask]];
        if (deleteIndex > 1)
        {
            [afterDelete replaceCharactersInRange:NSMakeRange(deleteIndex, phoneFormat.length-deleteIndex) withString:[phoneFormat substringFromIndex:deleteIndex]];
            
            deleteIndex--;
            
            if (deleteIndex > 1)
            {
                while (!isdigit([afterDelete characterAtIndex:deleteIndex]))
                    deleteIndex--;
            }
        }
        
        return afterDelete;
    }
    
    NSRange nextNumber = [input rangeOfString:phoneDigitMask];
    if (nextNumber.location != NSNotFound)
        deleteIndex = nextNumber.location;
    
    char lastInputChar = [phone characterAtIndex:[phone length] - 1];
    
    NSMutableString* number = [NSMutableString stringWithString:input];
    if (number.length > phoneFormat.length)
        [number replaceCharactersInRange:NSMakeRange(phoneFormat.length, 1) withString:@""];
    
    if (nextNumber.location == NSNotFound)
    {
        myPhoneSemaphore = 1;
        return number;
    }
    if (isdigit(lastInputChar))
        [number replaceCharactersInRange:nextNumber withString:[NSString stringWithFormat:@"%c", lastInputChar]];
    
    
    return number;
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
