#import "RootController.h"
#import "ChildController.h"

@interface RootController()
@property (retain, nonatomic) UIViewController* currentViewController;
- (UIViewController *)viewControllerByName:(NSString *)controllerName;
@end

@implementation RootController

NSString *const NAVIGTION_BG_PORTRAIT = @"navigation_bg_portrait.png";
NSString *const NAVIGTION_BG_LANDSCAPE = @"navigation_bg_landscape.png";

@synthesize review = _review;
@synthesize currentViewController = _currentViewController;
@synthesize reviewService = _reviewService;

#pragma mark - Initialization

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super initWithCoder:aDecoder]) {
        _reviewService = [[ReviewService alloc] init];
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
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    self.currentViewController = nil;
}

- (void)dealloc {
    [_currentViewController release];
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
