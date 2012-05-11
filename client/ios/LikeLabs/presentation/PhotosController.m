#import <AVFoundation/AVFoundation.h>
#import "PhotosController.h"
#import "RootController.h"
#import "PhotoPickerController.h"
#import "Review.h"

static NSString *const NAVBAR_BACKGROUND_IMG = @"navigation_bar_bg.png";
static NSString *const NAVIGATION_BACKGROUND_IMG = @"navigation_bg.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
static NSString *const GET_READY_MSG = @"Get Ready!";
static NSString *const PHOTO_N_OF_5_MSG = @"Picture %d of 5";

@interface PhotosController()
@property (retain,nonatomic) RootController* rootController;
@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, retain) PhotoPickerController *photoPicker;
@property (assign) NSInteger photoNumber;
- (void) initCapture;
- (void) pictureN;
- (void) countDown;
@end

@implementation PhotosController

@synthesize navBar = _navBar;
@synthesize contentView = _contentView;
@synthesize navigationBackground = _navigationBackground;
@synthesize segmentedControl = _segmentedControl;
@synthesize headerView = _headerView;
@synthesize instructionalView = _instructionalView;
@synthesize button = _button;
@synthesize whenYouAreReadyLabel = _whenYouAreReadyLabel;
@synthesize photosWillBeTakenLabel = _photosWillBeTakenLabel;
@synthesize getReadyLabel = _getReadyLabel;
@synthesize captureSession = _captureSession;
@synthesize rootController = _rootController;
@synthesize previewLayer = _previewLayer;
@synthesize photoPicker = _photoPicker;
@synthesize photoNumber = _photoNumber;

-(id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.rootController.review = [[Review alloc] initWithReviewType:ReviewTypePhoto];
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

-(void)viewDidLayoutSubviews {
    self.previewLayer.frame = self.contentView.bounds;
    [self.contentView bringSubviewToFront:self.instructionalView];
    [self.contentView bringSubviewToFront:self.button];
}

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
       
    self.instructionalView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"instruction_banner_bg.png"]];
    CALayer* layer = self.instructionalView.layer;
    layer.borderColor = [UIColor whiteColor].CGColor;
    self.photoNumber = 1;
    [self initCapture];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(saveImageToPhotoAlbum) name:kImageCapturedSuccessfully object:nil];
}

- (void)viewDidUnload
{
    [self setNavBar:nil];
    [self setContentView:nil];
    [self setRootController:nil];
    [self setNavigationBackground:nil];
    [self setSegmentedControl:nil];
    [self setHeaderView:nil];
    [self setInstructionalView:nil];
    [self setButton:nil];
    [self setCaptureSession:nil];
    [self setPreviewLayer:nil];
    [self setWhenYouAreReadyLabel:nil];
    [self setPhotosWillBeTakenLabel:nil];
    [self setGetReadyLabel:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    self.previewLayer.orientation = toInterfaceOrientation;
    
    CGSize viewSize = self.contentView.frame.size;
    CGFloat viewOffset = self.contentView.frame.origin.y;
    self.previewLayer.frame = CGRectMake(0, 0, viewSize.height + viewOffset , viewSize.width - viewOffset);
}

- (void)dealloc {
    [_navBar release];
    [_contentView release];
    [_rootController release];
    [_navigationBackground release];
    [_segmentedControl release];
    [_headerView release];
    [_instructionalView release];
    [_button release];
    [_captureSession release];
    [_previewLayer release];
    [_whenYouAreReadyLabel release];
    [_photosWillBeTakenLabel release];
    [_getReadyLabel release];
    [super dealloc];
}

- (IBAction)takePhotos:(id)sender {
    [UIView animateWithDuration:0.2 animations:^{
        self.whenYouAreReadyLabel.alpha = 
        self.photosWillBeTakenLabel.alpha = 0;
       
        self.getReadyLabel.alpha = 1;
    }];
    
    self.button.hidden = YES;
    self.segmentedControl.selectedSegmentIndex++;
    
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(pictureN) userInfo:nil repeats:NO];
}

- (void)getReady {
    if(!self.captureSession.isRunning) {
        [self.captureSession startRunning];
    }
    self.getReadyLabel.text = GET_READY_MSG;
    self.segmentedControl.selectedSegmentIndex++;
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(pictureN) userInfo:nil repeats:NO];
}

- (void)pictureN {
    self.getReadyLabel.text = [NSString stringWithFormat: PHOTO_N_OF_5_MSG, self.photoNumber++];
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(countDown) userInfo:nil repeats:NO];
}

- (void)countDown{
    _photoPicker = [[PhotoPickerController alloc] init];
    [self presentModalViewController:self.photoPicker animated:NO];
    [self.captureSession stopRunning];
}

- (void)saveImageToPhotoAlbum 
{
    [self dismissModalViewControllerAnimated:NO];
    [self.rootController.review.photos addObject: self.photoPicker.image];
    self.photoPicker = nil;
    [_photoPicker release];
    if (self.photoNumber <= 5) {
        [self getReady];
    } else {
        [self.rootController switchToController:@"PhotoSelectionController"];
    }
}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    if (error != NULL) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error!" message:@"Image couldn't be saved" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
    }
}

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}

- (void)initCapture {
    [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    NSArray* devices = [AVCaptureDevice devices];
    AVCaptureDevice* frontCamera = nil;
    for (AVCaptureDevice* device in devices) {
        if (device.position == AVCaptureDevicePositionFront) {
            frontCamera = device;
            break;
        }
    }
	AVCaptureDeviceInput *captureInput = [AVCaptureDeviceInput deviceInputWithDevice:frontCamera error:nil];

	_captureSession = [[AVCaptureSession alloc] init];
	[self.captureSession addInput:captureInput];    
    [self.captureSession setSessionPreset:AVCaptureSessionPresetMedium];
    
	self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession: self.captureSession];
	self.previewLayer.frame = self.contentView.bounds;
    self.previewLayer.orientation = [UIApplication sharedApplication].statusBarOrientation;
	self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
	[self.contentView.layer addSublayer: self.previewLayer];
    
	[self.captureSession startRunning];	
}
@end
