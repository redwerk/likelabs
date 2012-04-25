#import "LoginController.h"
#import "LoginService.h"

@implementation LoginController

@synthesize inputCode;
@synthesize inputPassword;
@synthesize submitButton;

NSString *bgLandscape = @"bg_landscape.png";
NSString *bgPortrait = @"bg_portrait.png";

-(void)viewDidLoad {
    UIColor *background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]];
    self.view.backgroundColor = background;
    inputCode.borderStyle = UITextBorderStyleRoundedRect;
    inputPassword.borderStyle = UITextBorderStyleRoundedRect;
    [super viewDidLoad];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    if(textField == inputCode) {
        [inputPassword becomeFirstResponder];
    } else if (textField == inputPassword) {
        [textField resignFirstResponder];
        [self formSubmit:nil];
    }
    return YES;
}

-(void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
    UIColor *background;
    UIInterfaceOrientation orientation = [[UIDevice currentDevice] orientation];
    
    if(orientation == UIInterfaceOrientationPortrait || orientation == UIInterfaceOrientationPortraitUpsideDown) {
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
    
    BOOL result;
    LoginService *loginService = [[LoginService alloc] init];
    
    result = [loginService checkLogin:self.inputCode.text andPassword:self.inputPassword.text];
    
    [LoginService release];
    
    UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"" message: result == false ? @"Incorrect credentials.":@"Login Succeeded!" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:@"Cancel", nil];
    [alert show];
    [alert release];   
}

@end
