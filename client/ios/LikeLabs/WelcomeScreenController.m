#import "WelcomeScreenController.h"
#import "RootController.h"

@interface WelcomeScreenController()
@property (retain,nonatomic) RootController* rootController;
@end

@implementation WelcomeScreenController

@synthesize rootController = _rootController;

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
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    self.rootController = nil;
}

- (void)dealloc {
    [_rootController release];
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (IBAction)showTextScreen:(id)sender {
    [self.rootController switchToController:@"TextReviewController"];
}

- (IBAction)showPhotoScreen:(id)sender {
    [self.rootController switchToController:@"PhotosController"];
}
@end
