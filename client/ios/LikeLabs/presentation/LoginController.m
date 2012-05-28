#import "LoginController.h"
#import "LoginService.h"
#import "RootController.h"

@interface LoginController()
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) UIAlertView * alertLogout;
@end

@implementation LoginController

NSString *const kLogoutViewDidDismiss = @"LogoutViewDidDismiss";

@synthesize inputCode = _inputCode;
@synthesize inputPassword = _inputPassword;
@synthesize submitButton = _submitButton;
@synthesize rootController = _rootController;
@synthesize alertLogout = _alertLogout;
@synthesize mode = _mode;

NSString *bgLandscape = @"bg_landscape.png";
NSString *bgPortrait = @"bg_portrait.png";


- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.mode = ControllerModeLogin;
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self willRotateToInterfaceOrientation:[self interfaceOrientation] duration:0];
}

-(void)viewDidLoad {   
    [super viewDidLoad];
    
    self.view.frame = [UIScreen mainScreen].bounds;
    
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
    self.alertLogout = nil;
}

- (void)dealloc {
    [_inputCode release];
    [_inputPassword release];
    [_submitButton release];
    [_rootController release];
    [_alertLogout release];
    [super dealloc];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if(textField == self.inputCode) {
        [self.inputPassword becomeFirstResponder];
    } else if (textField == self.inputPassword) {
        [textField resignFirstResponder];
        if (self.mode == ControllerModeLogin) {
            [self formSubmit:nil];
        } else {
            [self logout:nil];
        }
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
    BOOL loginSuccessfull = [self.rootController.loginService checkLogin:self.inputCode.text andPassword:self.inputPassword.text];
    
    if (loginSuccessfull) {
        [self.rootController switchToController:@"SplashScreenController"];
    } else {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"" message: @"Incorrect credentials." 
                                                        delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
        [alert release];   
    }    
}

- (IBAction)logout:(id)sender
{
    BOOL logoutSuccessfull = [self.rootController.loginService checkLogout:self.inputCode.text andPassword:self.inputPassword.text];
    
    if (logoutSuccessfull) {
        [[UIApplication sharedApplication].delegate applicationWillTerminate:[UIApplication sharedApplication]];
        exit(0);
    } else {
        _alertLogout = [[UIAlertView alloc] initWithTitle:@"" message: @"Incorrect credentials." 
                                                        delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [_alertLogout show];
    }    
}

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (alertView == self.alertLogout)
    {
        [[NSNotificationCenter defaultCenter] postNotificationName:kLogoutViewDidDismiss object:nil];
    }
}

- (void)setSubmitButtonName:(NSString *)name
{
    [self.submitButton setTitle:name forState:UIControlStateNormal];
}

@end
