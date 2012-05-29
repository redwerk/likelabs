#import "RootController.h"
#import "ChildController.h"
#import "SettingsService.h"
#import "SettingsDao.h"

@interface RootController()
@property (retain, nonatomic) UIViewController* currentViewController;
@property (assign, nonatomic) SettingsService* settingsService;
@property (assign, nonatomic) SettingsDao* dao;
@end

@implementation RootController
static NSUInteger const HOURS_24 = 86400;
NSString *const NAVIGTION_BG_PORTRAIT = @"navigation_bg_portrait.png";
NSString *const NAVIGTION_BG_LANDSCAPE = @"navigation_bg_landscape.png";
CGFloat const SLIDE_SPEED = 0.5;

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
    
    UIViewController *vc = [RootController viewControllerByName:@"LoginController" rootController:self];
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

+ (UIViewController *) viewControllerByName:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root{
    return [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:root] autorelease];
}

+ (void) switchToController:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root {
    UIViewController *vc = [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:root] autorelease];
    vc.view.frame = root.view.bounds;
    [root addChildViewController:vc];
    [root.view addSubview:vc.view];
    [vc.view layoutSubviews];
    [vc.view setNeedsLayout];
    vc.view.center = CGPointMake(vc.view.frame.size.width*1.5, vc.view.frame.size.height/2);
    [UIView animateWithDuration:SLIDE_SPEED animations: ^{

        [root getCurrentController].view.center = CGPointMake(-0.5*vc.view.frame.size.width, vc.view.frame.size.height/2);
        vc.view.center = CGPointMake((vc.view.frame.size.width/2), vc.view.frame.size.height/2);
    } completion:^(BOOL finished){
        [[root getCurrentController].view removeFromSuperview];
        [vc didMoveToParentViewController:root];
        [[root getCurrentController] removeFromParentViewController];
        [root setCurrentController:vc];
    }];

     
     
    if([root respondsToSelector:@selector(bringHeaderViewToFront)]){
        [root bringHeaderViewToFront];
    }
}

+ (void) switchBackToController:(NSString *)controllerName rootController:(UIViewController<ContainerController> *)root{
    UIViewController *vc = [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:root] autorelease];
    [root.view addSubview:vc.view];
    [root addChildViewController:vc];
    vc.view.frame = root.view.bounds; 
    vc.view.center = CGPointMake(vc.view.frame.size.width*-.5, vc.view.frame.size.height/2);

    [UIView animateWithDuration:SLIDE_SPEED animations: ^{

        [root getCurrentController].view.center = CGPointMake(vc.view.frame.size.width*1.5, vc.view.frame.size.height/2);
        vc.view.center = CGPointMake((vc.view.frame.size.width/2), vc.view.frame.size.height/2);
    } completion:^(BOOL finished){
        [[root getCurrentController].view removeFromSuperview];
        [vc didMoveToParentViewController:root];
        [[root getCurrentController] removeFromParentViewController];
        [root setCurrentController:vc];
    }];

    if([root respondsToSelector:@selector(bringHeaderViewToFront)]){
        [root bringHeaderViewToFront];
    }
}

- (UIViewController *) getCurrentController{
    return self.currentViewController;
}

- (void) setCurrentController:(UIViewController *)controller{
    self.currentViewController = controller;
}

@end
