#import <AVFoundation/AVFoundation.h>
#import "PhotoMessageController.h"
#import "RootController.h"

@interface PhotoMessageController ()
@property (nonatomic,retain) RootController *rootController;
@property (assign) BOOL textPlaseholderActive;
@end

@implementation PhotoMessageController
@synthesize segmentedControl = _segmentedControl;
@synthesize navigationBackground = _navigationBackground;
@synthesize textView = _textView;
@synthesize imageView = _imageView;

static NSString *const bgLandscape = @"bg_landscape.png";
static NSString *const bgPortrait = @"bg_portrait.png";
static NSString *const NAVIGATION_BACKGROUND_IMG = @"navigation_bg.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
static NSString *const GREETING = @"Start typing a message!";

@synthesize rootController = _rootController;
@synthesize textPlaseholderActive = _textPlaseholderActive;

-(id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    UIColor *background = [[UIColor alloc] initWithPatternImage:
                           [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.view.backgroundColor = background;
    [background release];
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
    [self.segmentedControl setEnabled:NO forSegmentAtIndex:3];
    self.segmentedControl.selectedSegmentIndex = 1;
    
    self.textPlaseholderActive = true;
    self.textView.layer.borderColor = [UIColor blackColor].CGColor;
    self.textView.layer.borderWidth = 1;
    self.textView.layer.cornerRadius = 10;    
    [self.textView becomeFirstResponder];
    
    
    [self setPhoto:[self.rootController.review.photos objectAtIndex:self.rootController.review.reviewPhotoIndex]];
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOffset = CGSizeMake(5, 5);
    self.imageView.layer.shadowOpacity = 0.8;
    self.imageView.layer.shadowRadius = 10;
}

- (void) setPhoto: (UIImage *)photo {
    CGSize maxPhotoSize = CGSizeMake(409, 296);
    CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
    photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
    
    self.imageView.image = photo;
    
    CGPoint oldCenter = self.imageView.center;
    self.imageView.frame = CGRectMake(0, 0, photo.size.width, photo.size.height);
    self.imageView.center = oldCenter;
}

- (void)viewDidUnload
{
    [self setSegmentedControl:nil];
    [self setNavigationBackground:nil];
    [self setTextView:nil];
    [self setImageView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    [self setRootController:nil];
}

- (void)dealloc {
    [_navigationBackground release];
    [_segmentedControl release];
    [_rootController release];
    [_textView release];
    [_imageView release];
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft || interfaceOrientation == UIInterfaceOrientationLandscapeRight);
}

- (BOOL)textView:(UITextView *)view shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (self.textPlaseholderActive) {
        self.textView.text = @"";
        self.textView.textColor = [UIColor blackColor];
        self.textPlaseholderActive = false;
    } 
    return YES;
}

- (void)textViewDidChange:(UITextView* ) view {
    if (self.textView.text.length == 0) {
        self.textView.textColor = [UIColor lightGrayColor];
        self.textView.text = GREETING;
        self.textPlaseholderActive = true;
    }
}

- (IBAction)navigationChaned:(UISegmentedControl *)sender {
    if (sender.selectedSegmentIndex == 0) {
        [self.rootController switchToController:@"PhotoSelectionController"];
    }
}

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}
@end
