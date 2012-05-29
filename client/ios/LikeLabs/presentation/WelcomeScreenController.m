#import "WelcomeScreenController.h"
#import "RootController.h"
#import "LoginController.h"
#import "SettingsDao.h"
#import <AVFoundation/AVFoundation.h>

static NSString *const bgLandscape = @"welcome_landscape_bg.png";
static NSString *const bgPortrait = @"welcome_portrait_bg.png";
static NSString *const WELCOME_VENDOR_MSG = @"Welcome to the %@ Social Hub!";

@interface WelcomeScreenController()
@property (retain,nonatomic) RootController* rootController;
@property (nonatomic, retain) LoginController* overlayLogout;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (void) setLogo: (UIImage *)logo;
@end

@implementation WelcomeScreenController
@synthesize btnExit = _btnExit;
@synthesize btnShowText = _btnShowText;
@synthesize btnShowPhoto = _btnShowPhoto;
@synthesize btnFacebook = _btnFacebook;
@synthesize btnVkcom = _btnVkcom;
@synthesize btnTweeter = _btnTweeter;
@synthesize btnEmail = _btnEmail;
@synthesize imageCompany = _imageCompany;
@synthesize btnHome = _btnHome;
@synthesize textLabel = _textLabel;
@synthesize overlayLogout = _overlayLogout;

@synthesize rootController = _rootController;

-(id)initWithRootController:(RootController *)rootController 
{
    if (self = [super init]) 
    {
        self.rootController = rootController;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dismissOverlay) name:kLogoutViewDidDismiss object:nil];
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
    
    UIColor *background = [[UIColor alloc] initWithPatternImage:
                           [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.view.backgroundColor = background;
    [background release];
        
    SettingsDao* dao = [[SettingsDao alloc] init];
    [self setLogo:dao.logo];
       
    [self.textLabel setFont:[UIFont fontWithName:@"Lobster 1.4" size:50]];
    [self.textLabel setText:[NSString stringWithFormat: WELCOME_VENDOR_MSG, dao.companyName]];
    [dao release];
    
    [self layoutSubviewsForInterfaceOrientation:self.interfaceOrientation];
}

- (void) setLogo: (UIImage *)logo {

    CGSize const MAX_LOGO_SIZE = CGSizeMake(232, 85);
    if(logo.size.height > MAX_LOGO_SIZE.height || logo.size.width > MAX_LOGO_SIZE.width) {
        CGFloat scale = MIN(MAX_LOGO_SIZE.width / logo.size.width, MAX_LOGO_SIZE.height / logo.size.height);
        logo = [UIImage imageWithCGImage:logo.CGImage scale:1.0/scale orientation:logo.imageOrientation];
    }
    self.imageCompany.image = logo;
    
    CGPoint oldCenter = self.imageCompany.center;
    self.imageCompany.frame = CGRectMake(0, 0, logo.size.width, logo.size.height);
    self.imageCompany.center = oldCenter;
}

- (void)viewDidUnload
{
    [self setBtnExit:nil];
    [self setBtnShowText:nil];
    [self setBtnShowPhoto:nil];
    [self setBtnFacebook:nil];
    [self setBtnVkcom:nil];
    [self setBtnTweeter:nil];
    [self setBtnEmail:nil];
    [self setImageCompany:nil];
    [self setBtnHome:nil];
    [self setTextLabel:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    self.rootController = nil;
}

- (void)dealloc 
{
    [_rootController release];
    [_btnExit release];
    [_btnShowText release];
    [_btnShowPhoto release];
    [_btnFacebook release];
    [_btnVkcom release];
    [_btnTweeter release];
    [_btnEmail release];
    [_imageCompany release];
    [_btnHome release];
    [_textLabel release];
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration
{
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
    [self.overlayLogout willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation 
{
    UIColor *background;    
    if (UIInterfaceOrientationIsPortrait(orientation))
    {
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]];
        
        self.btnExit.frame = CGRectMake(17, 991, 17, 18);
        self.imageCompany.frame = CGRectMake(268, 49, 232, 90);
        self.btnShowText.frame = CGRectMake(186, 756, 392, 202);
        self.btnShowPhoto.frame = CGRectMake(186, 516, 392, 202);
        self.btnFacebook.frame = CGRectMake(266, 419, 53, 53);
        self.btnVkcom.frame = CGRectMake(326, 419, 53, 53);
        self.btnTweeter.frame = CGRectMake(386, 419, 53, 53);
        self.btnEmail.frame = CGRectMake(446, 419, 53, 53);
        self.textLabel.frame = CGRectMake(20, 166, 728, 150);
    } else {
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]];
        
        self.btnExit.frame = CGRectMake(17, 733, 17, 18);
        self.imageCompany.frame = CGRectMake(397, 45, 232, 90);
        self.btnShowText.frame = CGRectMake(97, 421, 392, 202);
        self.btnShowPhoto.frame = CGRectMake(537, 421, 392, 202);
        self.btnFacebook.frame = CGRectMake(396, 316, 53, 53);
        self.btnVkcom.frame = CGRectMake(456, 316, 53, 53);
        self.btnTweeter.frame = CGRectMake(516, 316, 53, 53);
        self.btnEmail.frame = CGRectMake(576, 316, 53, 53);
        self.textLabel.frame = CGRectMake(20, 172, 984, 64);
    }
    self.view.backgroundColor = background;
    [background release];
}

- (void)dismissOverlay {
    [self.overlayLogout.view removeFromSuperview];
    self.overlayLogout = nil;
    [_overlayLogout release];
}

- (IBAction)showTextScreen:(id)sender {
    [self.rootController switchToController:@"RootMessageController"];
}

- (IBAction)showPhotoScreen:(id)sender {
    [self.rootController switchToController:@"PhotosController"];
}

- (IBAction)exitApp:(id)sender 
{
    _overlayLogout = [[LoginController alloc] initWithRootController:self.rootController];
    self.overlayLogout.mode = ControllerModeLogout;
    [self.rootController.view addSubview:self.overlayLogout.view];
    [self.overlayLogout setSubmitButtonName:@"Logout"];
    [self.overlayLogout.submitButton removeTarget:self.overlayLogout action:@selector(formSubmit:) forControlEvents:UIControlEventTouchUpInside];
    [self.overlayLogout.submitButton addTarget:self.overlayLogout action:@selector(logout:) forControlEvents:UIControlEventTouchUpInside];
}

- (IBAction)shareFacebook:(id)sender {
}

- (IBAction)shareVkontakte:(id)sender {
}

- (IBAction)shareTweeter:(id)sender {
}

- (IBAction)shareEmail:(id)sender {
}

- (IBAction)showHome:(id)sender 
{
    [self.rootController switchBackToController:@"SplashScreenController" rootController:self.rootController];
}
@end
