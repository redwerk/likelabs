#import "WelcomeScreenController.h"
#import "RootController.h"
#import "LoginController.h"
#import "SettingsDao.h"
#import <AVFoundation/AVFoundation.h>

static NSString *const bgLandscape = @"welcome_bg_landscape.png";
static NSString *const bgPortrait = @"welcome_bg_portrait.png";
static NSString *const WELCOME_VENDOR_MSG = @"Welcome to the %@ Social Hub!";

@interface WelcomeScreenController()
@property (nonatomic, retain) RootController* rootController;
@property (nonatomic, retain) LoginController* overlayLogout;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (void) setLogo: (UIImage *)logo;
@end

@implementation WelcomeScreenController
@synthesize btnExit = _btnExit;
@synthesize imageCompany = _imageCompany;
@synthesize btnHome = _btnHome;
@synthesize textLabel = _textLabel;
@synthesize poweredBy = _poweredBy;
@synthesize textBtnView = _textBtnView;
@synthesize photoBtnView = _photoBtnView;
@synthesize logoView = _logoView;
@synthesize socialsView = _socialsView;
@synthesize overlayLogout = _overlayLogout;

@synthesize rootController = _rootController;

-(id)initWithRootController:(RootController *)rootController 
{
    if (self = [super init]) 
    {
        self.rootController = rootController;
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
    
    self.view.backgroundColor = [UIColor colorWithPatternImage: [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.textBtnView.backgroundColor = self.photoBtnView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"welcome_btn_bg.png"]];
    self.logoView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"welcome_logo_bg.png"]];
    self.socialsView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"socials_bg.png"]];
        
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
    [self setImageCompany:nil];
    [self setBtnHome:nil];
    [self setTextLabel:nil];
    [self setPoweredBy:nil];
    [self setTextBtnView:nil];
    [self setPhotoBtnView:nil];
    [self setLogoView:nil];
    [self setSocialsView:nil];
    [super viewDidUnload];
}

- (void)dealloc 
{
    self.rootController = nil;
    self.overlayLogout = nil;
    [_btnExit release];
    [_imageCompany release];
    [_btnHome release];
    [_textLabel release];
    [_poweredBy release];
    [_textBtnView release];
    [_photoBtnView release];
    [_logoView release];
    [_socialsView release];
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration
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
        
        self.btnExit.frame = CGRectMake(17, 981, 32, 33);
        self.logoView.frame = CGRectMake(217, 43, 332, 101);
        self.textBtnView.frame = CGRectMake(182, 751, 400, 211);
        self.photoBtnView.frame = CGRectMake(182, 511, 400, 211);
        self.socialsView.frame = CGRectMake(258, 410, 249, 71);
        self.textLabel.frame = CGRectMake(20, 166, 728, 150);
        self.poweredBy.frame = CGRectMake(555, 989, 203, 30);
    } else {
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]];
        
        self.btnExit.frame = CGRectMake(17, 725, 32, 33);
        self.logoView.frame = CGRectMake(346, 39, 332, 101);
        self.textBtnView.frame = CGRectMake(93, 416, 400, 211);
        self.photoBtnView.frame = CGRectMake(533, 416, 400, 211);
        self.socialsView.frame = CGRectMake(388, 307, 249, 71);
        self.textLabel.frame = CGRectMake(20, 172, 984, 64);
        self.poweredBy.frame = CGRectMake(811, 733, 203, 30);
    }
    self.view.backgroundColor = background;
    [background release];
}

- (IBAction)showTextScreen:(id)sender {
    [RootController switchToController:@"RootMessageController" rootController:self.rootController];
}

- (IBAction)showPhotoScreen:(id)sender {
    [RootController switchToController:@"PhotoPickerController" rootController:self.rootController];
}

- (IBAction)exitApp:(id)sender 
{
    _overlayLogout = [[LoginController alloc] initWithRootController:self.rootController];
    [RootController switchBackToViewController:_overlayLogout rootController:self.rootController];
    [_overlayLogout setControllerMode:ControllerModeLogout];
}

- (IBAction)showHome:(id)sender 
{
    [self.rootController goHome];
}
@end
