#import "RootController.h"
#import "ChildController.h"
#import "SettingsService.h"
#import "SettingsDao.h"

@interface RootController()
@property (retain, nonatomic) UIViewController* currentViewController;
@property (assign, nonatomic) SettingsService* settingsService;
@property (assign, nonatomic) SettingsDao* dao;
- (UIViewController *)viewControllerByName:(NSString *)controllerName;
@end

@implementation RootController
static NSUInteger const HOURS_24 = 86400;
NSString *const NAVIGTION_BG_PORTRAIT = @"navigation_bg_portrait.png";
NSString *const NAVIGTION_BG_LANDSCAPE = @"navigation_bg_landscape.png";

@synthesize review = _review;
@synthesize currentViewController = _currentViewController;
@synthesize reviewService = _reviewService;
@synthesize loginService = _loginService;
@synthesize settingsService = _settingsService;
@synthesize dao = _dao;

#pragma mark - Initialization

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super initWithCoder:aDecoder]) {
        _reviewService = [[ReviewService alloc] init];
        _loginService = [[LoginService alloc] init];
        _settingsService = self.loginService.settingsService;
        _dao = self.loginService.dao;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    
    UIViewController *vc = [self viewControllerByName:@"LoginController"];
    [self addChildViewController:vc];
    vc.view.frame = self.view.bounds;
    [self.view addSubview:vc.view];
    self.currentViewController = vc;
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [super viewDidUnload];
    self.currentViewController = nil;
    self.reviewService = nil;
    self.loginService = nil;
    self.review = nil;
}

- (void)dealloc {
    [_currentViewController release];    
    [_reviewService release];
    [_loginService release];
    [_review release];
    [super dealloc];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    if ([self.currentViewController shouldAutorotateToInterfaceOrientation:toInterfaceOrientation]) {
        [self.currentViewController willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    }
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    if ([self.currentViewController shouldAutorotateToInterfaceOrientation:fromInterfaceOrientation]) {
        [self.currentViewController didRotateFromInterfaceOrientation:fromInterfaceOrientation];
    }
}

#pragma mark - ContainerController implementation

- (UIViewController *)viewControllerByName:(NSString *)controllerName {
    return [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:self] autorelease];
}

- (void)switchToController:(NSString *)controllerName {
    
    if ([controllerName isEqualToString:@"SplashScreenController"] && [[NSDate date] timeIntervalSinceDate:self.dao.lastUpdate] > HOURS_24) {            
        [self.settingsService getSettings];        
    }
    
    UIViewController *vc = [self viewControllerByName:controllerName];
    [self addChildViewController:vc];
    [self transitionFromViewController:self.currentViewController toViewController:vc duration:0.5 options:UIViewAnimationOptionTransitionFlipFromLeft animations: ^{
        [self.currentViewController.view removeFromSuperview];
        vc.view.frame = self.view.bounds;
        [self.view addSubview:vc.view];
    } completion:^(BOOL finished) {
        [vc didMoveToParentViewController:self];
        [self.currentViewController removeFromParentViewController];
        self.currentViewController = vc;
    }];
}

@end
