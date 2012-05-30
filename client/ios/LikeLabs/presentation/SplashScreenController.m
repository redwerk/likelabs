#import "SplashScreenController.h"
#import "RootController.h"
#import "SettingsDao.h"

@interface SplashScreenController()
@property (retain, nonatomic) RootController* rootController;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
@end;

@implementation SplashScreenController
@synthesize startBtn = _startBtn;
@synthesize socialButtonsView = _socialButtonsView;
@synthesize shareYourSmileImg = _shareYourSmileImg;
@synthesize companyLogo = _companyLogo;

@synthesize rootController = _rootController;

#pragma mark - View lifecycle

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    SettingsDao* dao = [[SettingsDao alloc] init];
    [self setLogo:dao.logo];
    [dao release];
    [self layoutSubviewsForInterfaceOrientation:self.interfaceOrientation];
}

- (void)viewDidUnload
{
    [self setStartBtn:nil];
    [self setSocialButtonsView:nil];
    [self setShareYourSmileImg:nil];
    [self setCompanyLogo:nil];
    [super viewDidUnload];
    self.rootController = nil;
}

- (void)dealloc {
    [_rootController release];
    [_startBtn release];
    [_socialButtonsView release];
    [_shareYourSmileImg release];
    [_companyLogo release];
    [super dealloc];
}

- (void) setLogo: (UIImage *)logo {
    
    CGSize const MAX_LOGO_SIZE = CGSizeMake(232, 85);
    if(logo.size.height > MAX_LOGO_SIZE.height || logo.size.width > MAX_LOGO_SIZE.width) {
        CGFloat scale = MIN(MAX_LOGO_SIZE.width / logo.size.width, MAX_LOGO_SIZE.height / logo.size.height);
        logo = [UIImage imageWithCGImage:logo.CGImage scale:1.0/scale orientation:logo.imageOrientation];
    }
    self.companyLogo.image = logo;
    
    CGPoint oldCenter = self.companyLogo.center;
    self.companyLogo.frame = CGRectMake(0, 0, logo.size.width, logo.size.height);
    self.companyLogo.center = oldCenter;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation {
    UIColor* background;    
    if (UIInterfaceOrientationIsPortrait(orientation)) { //PORTRAIT
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"splash_bg_portrait.png"]];        
        self.startBtn.frame = CGRectMake(305, 295, 158, 158);
        self.socialButtonsView.center = CGPointMake(384, 230.5);
        self.shareYourSmileImg.frame = CGRectMake(128, 125, 507, 51);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_portrait.png"];
        self.companyLogo.frame = CGRectMake(268, 17, 232, 85);
    } else { //LANDSCAPE
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"splash_bg_landscape.png"]];
        self.startBtn.frame = CGRectMake(701, 444, 272, 272);
        self.socialButtonsView.center = CGPointMake(837, 342);
        self.shareYourSmileImg.frame = CGRectMake(682, 180, 310, 98);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_landscape.png"];
        self.companyLogo.frame = CGRectMake(721, 47, 232, 85);
    }
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)showWelcomeScreen:(id)sender {
    [RootController switchToController:@"WelcomeScreenController" rootController:self.rootController];
}
@end
