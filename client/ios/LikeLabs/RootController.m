#import "RootController.h"
#import "ChildController.h"

@interface RootController()
@property (retain, nonatomic) UIViewController* currentViewController;
- (UIViewController *)viewControllerByName:(NSString *)controllerName;
@end

@implementation RootController

@synthesize currentViewController = _currentViewController;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
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
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    UIViewController *vc = [self viewControllerByName:@"LoginController"];
    [self addChildViewController:vc];
    vc.view.frame = self.view.bounds;
    [self.view addSubview:vc.view];
    self.currentViewController = vc;
}

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

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self.currentViewController willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self.currentViewController didRotateFromInterfaceOrientation:fromInterfaceOrientation];
}

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
