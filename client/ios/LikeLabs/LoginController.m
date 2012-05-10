#import "LoginController.h"
#import "LoginService.h"
#import "RootController.h"

@interface LoginController()
@property (retain, nonatomic) RootController* rootController;
@end

@implementation LoginController

@synthesize inputCode = _inputCode;
@synthesize inputPassword = _inputPassword;
@synthesize submitButton = _submitButton;
@synthesize rootController = _rootController;

NSString *bgLandscape = @"bg_landscape.png";
NSString *bgPortrait = @"bg_portrait.png";


- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
    }
    return self;
}

-(void)viewDidLoad {   
    [super viewDidLoad];    
    UIColor *background = [[UIColor alloc] initWithPatternImage:
                           [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.view.backgroundColor = background;
    [background release];
    self.inputCode.borderStyle = UITextBorderStyleRoundedRect;
    self.inputPassword.borderStyle = UITextBorderStyleRoundedRect;    
}

- (void)viewDidUnload {
    [super viewDidUnload];
    self.inputCode = nil;
    self.inputPassword = nil;
    self.submitButton = nil;
    self.rootController = nil;
}

- (void)dealloc {
    [_inputCode release];
    [_inputPassword release];
    [_submitButton release];
    [_rootController release];
    [super dealloc];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if(textField == self.inputCode) {
        [self.inputPassword becomeFirstResponder];
    } else if (textField == self.inputPassword) {
        [textField resignFirstResponder];
        [self formSubmit:nil];
    }
    return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
    UIColor *background;   
    if(toInterfaceOrientation == UIInterfaceOrientationPortrait || toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) {
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]];
    } else {
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]];
    }
    self.view.backgroundColor = background;
    [background release];
    [super viewDidLoad];
} 

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

- (IBAction)formSubmit:(id)sender {    
    LoginService *loginService = [[LoginService alloc] init];
    BOOL loginSuccessfull = [loginService checkLogin:self.inputCode.text andPassword:self.inputPassword.text];
    [loginService release];
    
    if (loginSuccessfull) {
        [self.rootController switchToController:@"SplashScreenController"];
    } else {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"" message: @"Incorrect credentials." 
                                                        delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Ok", nil];
        [alert show];
        [alert release];   
    }    
}

@end
