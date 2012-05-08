#import "PhotosController.h"
#import "RootController.h"

static NSString *const NAVBAR_BACKGROUND_IMG = @"navigation_bar_bg.png";
static NSString *const NAVIGATION_BACKGROUND_IMG = @"navigation_bg.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";

@interface PhotosController()
@property (retain,nonatomic) RootController* rootController;
@property (retain, nonatomic) UIViewController* currentViewController;
- (UIViewController *)viewControllerByName:(NSString *)controllerName;
@end

@implementation PhotosController

@synthesize navBar = _navBar;
@synthesize contentView = _contentView;
@synthesize navigationBackground = _navigationBackground;
@synthesize segmentedControl = _segmentedControl;
@synthesize headerView = _headerView;
@synthesize rootController = _rootController;
@synthesize currentViewController = _currentViewController;


-(id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
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
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    self.navBar.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:NAVBAR_BACKGROUND_IMG]];
    self.navigationBackground.image = [[UIImage imageNamed:NAVIGATION_BACKGROUND_IMG] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    [self.navigationBackground setContentMode:UIViewContentModeScaleToFill];
    [self.segmentedControl setBackgroundImage:[UIImage imageNamed:NAV_BTN_NORMAL_IMG] forState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setBackgroundImage:[UIImage imageNamed:NAV_BTN_SELECTED_IMG] forState:UIControlStateSelected barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_NN_IMG] 
                                 forLeftSegmentState:UIControlStateNormal rightSegmentState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_SN_IMG] 
                                 forLeftSegmentState:UIControlStateSelected rightSegmentState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_NS_IMG] 
                                 forLeftSegmentState:UIControlStateNormal rightSegmentState:UIControlStateSelected barMetrics:UIBarMetricsDefault];
    
//    [self.segmentedControl setSegmentedControlStyle:UISegmentedControlStyleBar];
//    [self.segmentedControl setContentMode:UIViewContentModeScaleToFill];
//    [self.segmentedControl setWidth:197.0 forSegmentAtIndex:0];
//    [self.segmentedControl setWidth:191.0 forSegmentAtIndex:6];
    
    /*
     TakePhoto
     GetReady
     Picture#
     Short
     */
    
    UIViewController *vc = [self viewControllerByName:@"TakePhotoController"];
    [self addChildViewController:vc];
    vc.view.frame = self.contentView.frame;
    [self.view addSubview:vc.view];
    self.currentViewController = vc;
}

- (void)viewDidUnload
{
    [self setNavBar:nil];
    [self setContentView:nil];
    [self setRootController:nil];
    [self setNavigationBackground:nil];
    [self setSegmentedControl:nil];
    [self setHeaderView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (UIViewController *)viewControllerByName:(NSString *)controllerName {
    return [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:self] autorelease];
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self.currentViewController willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self.currentViewController didRotateFromInterfaceOrientation:fromInterfaceOrientation];
}

- (void)switchToController:(NSString *)controllerName {
    UIViewController *vc = [self viewControllerByName:controllerName];
    [self addChildViewController:vc];
    [self transitionFromViewController:self.currentViewController toViewController:vc duration:0.5 options:UIViewAnimationOptionTransitionFlipFromLeft animations: ^{
        [self.currentViewController.view removeFromSuperview];
        vc.view.frame = self.contentView.frame;
        [self.contentView addSubview:vc.view];
    } completion:^(BOOL finished) {
        [vc didMoveToParentViewController:self];
        [self.currentViewController removeFromParentViewController];
    self.currentViewController = vc;
    }];
}

- (void)dealloc {
    [_navBar release];
    [_contentView release];
    [_rootController release];
    [_navigationBackground release];
    [_segmentedControl release];
    [_headerView release];
    [super dealloc];
}

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}
@end
