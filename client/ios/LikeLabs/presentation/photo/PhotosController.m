#import <AVFoundation/AVFoundation.h>
#import "PhotosController.h"
#import "RootController.h"
#import "PhotoPickerController.h"
#import "Review.h"

static NSString *const NAVBAR_BACKGROUND_IMG = @"navigation_bar_bg.png";
static NSString *const NAVIGATION_BACKGROUND_IMG = @"navigation_bg_landscape.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
static NSString *const GET_READY_MSG = @"Get Ready!";
static NSString *const PHOTO_N_OF_5_MSG = @"Picture %d of 5";
static float const TIMER_DELAY = 0.2;

@interface PhotosController()
@property (retain,nonatomic) RootController* rootController;
@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, retain) PhotoPickerController *photoPicker;
@property (assign) NSInteger photoNumber;
@property (nonatomic, retain) CustomizableSegmentedControl* customSegmentedControl;
@property (nonatomic, retain) UIViewController* currentViewController;

- (NSMutableArray*) getButtons;
- (NSMutableArray*) getWidths;
- (NSMutableDictionary*) getDividersDictionary;
- (void) initCapture;
- (void) pictureN;
- (void) countDown;
- (void) setPhotoTitlesForInterfaceOrientation: (UIInterfaceOrientation) orientation;
@end

@implementation PhotosController

@synthesize navBar = _navBar;
@synthesize contentView = _contentView;
@synthesize navigationBackground = _navigationBackground;
@synthesize segmentedControl = _segmentedControl;
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
@synthesize customSegmentedControl = _customSegmentedControl;
@synthesize currentViewController = _currentViewController;


#pragma mark - Initialization

-(id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.rootController.review = [[[Review alloc] initWithReviewType:ReviewTypePhoto] autorelease];
    }
    return self;
}

-(void)viewDidLayoutSubviews {
    self.previewLayer.frame = self.contentView.bounds;
    [self.contentView bringSubviewToFront:self.instructionalView];
    [self.contentView bringSubviewToFront:self.button];
    
    [self setPhotoTitlesForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    self.navBar.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:NAVBAR_BACKGROUND_IMG]];
    self.navigationBackground.image = [[UIImage imageNamed:NAVIGATION_BACKGROUND_IMG] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    [self.navigationBackground setContentMode:UIViewContentModeScaleToFill];       
    
    self.segmentedControl.alpha = 0;
    self.customSegmentedControl = [[[CustomizableSegmentedControl alloc] initWithFrame:self.segmentedControl.frame buttons:[self getButtons] widths:[self getWidths] dividers:[self getDividersDictionary] dividerWidth:22 delegate:self] autorelease];
    self.customSegmentedControl.autoresizingMask = self.segmentedControl.autoresizingMask;
    self.customSegmentedControl.selectedSegmentIndex = 1;
    self.customSegmentedControl.userInteractionEnabled = NO;
    [self.navBar addSubview:self.customSegmentedControl];
    
    self.instructionalView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"instruction_banner_bg.png"]];
    CALayer* layer = self.instructionalView.layer;
    layer.borderColor = [UIColor whiteColor].CGColor;
    self.photoNumber = 1;
    [self initCapture];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(saveImageToPhotoAlbum) name:kImageCapturedSuccessfully object:nil];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setNavBar:nil];
    [self setContentView:nil];
    [self setRootController:nil];
    [self setNavigationBackground:nil];
    [self setSegmentedControl:nil];
    [self setInstructionalView:nil];
    [self setButton:nil];
    [self setCaptureSession:nil];
    [self setPreviewLayer:nil];
    [self setWhenYouAreReadyLabel:nil];
    [self setPhotosWillBeTakenLabel:nil];
    [self setGetReadyLabel:nil];
    [self setCustomSegmentedControl:nil];
    [self setCurrentViewController:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [_navBar release];
    [_contentView release];
    [_rootController release];
    [_navigationBackground release];
    [_segmentedControl release];
    [_instructionalView release];
    [_button release];
    [_captureSession release];
    [_previewLayer release];
    [_whenYouAreReadyLabel release];
    [_photosWillBeTakenLabel release];
    [_getReadyLabel release];
    [_customSegmentedControl release];
    [_currentViewController release];
    [super dealloc];
}

#pragma mark - CustomSegmentedControl population

- (NSMutableArray*) getWidths {
    return [[[NSMutableArray alloc] initWithObjects:
             [NSNumber numberWithFloat:174],
             [NSNumber numberWithFloat:0],
             [NSNumber numberWithFloat:0],
             [NSNumber numberWithFloat:0],
             [NSNumber numberWithFloat:0],
             [NSNumber numberWithFloat:0],
             [NSNumber numberWithFloat:168], nil] autorelease];
}

- (NSMutableArray*) getButtons {
    NSMutableArray* buttons = [[[NSMutableArray alloc] initWithCapacity:5] autorelease];
    
    UIButton* firstButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* firstImg = [[UIImage imageNamed:@"btn_first_bg_selected.png"] stretchableImageWithLeftCapWidth:20 topCapHeight:0];
    UIColor* whiteColor = [UIColor whiteColor];
    [firstButton setBackgroundImage:firstImg forState:UIControlStateNormal];
    [firstButton setImage:[UIImage imageNamed:@"1.png"] forState:UIControlStateNormal];
    [firstButton setTitle:@"  Take photo" forState:UIControlStateNormal];
    [firstButton setTitleColor:whiteColor forState:UIControlStateNormal];
    [firstButton setEnabled:YES];
    [firstButton setSelected:NO];
    
    [buttons addObject:firstButton];    
    
    UIImage* imgSelected = [[UIImage imageNamed:@"btn_bg_highlighted.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* imgDisabled = [[UIImage imageNamed:@"btn_bg_disabled.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    for (NSUInteger i=1; i<6; i++) {
        UIButton* btn = [UIButton buttonWithType:UIButtonTypeCustom];
        [btn setBackgroundImage:imgSelected forState:UIControlStateSelected];
        [btn setBackgroundImage:imgDisabled forState:UIControlStateDisabled];
        NSString* title = [NSString stringWithFormat:@"Photo %d", i];
        [btn setTitle:title forState:UIControlStateDisabled];
        [btn setTitle:title forState:UIControlStateSelected];
        [btn setTitleColor:whiteColor forState:UIControlStateSelected];
        [btn setTitleColor:whiteColor forState:UIControlStateDisabled];
        [btn setEnabled:(i==1)];
        [btn setSelected:(i==1)];
        
        [buttons addObject:btn];
    }
    
    UIButton* lastBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* lastImg = [[UIImage imageNamed:@"btn_last_bg_disabled.png"] stretchableImageWithLeftCapWidth:20 topCapHeight:0];
    [lastBtn setBackgroundImage:lastImg forState:UIControlStateDisabled];
    [lastBtn setImage:[UIImage imageNamed:@"2.png"] forState:UIControlStateDisabled];
    [lastBtn setTitle:@"  Photo selection" forState:UIControlStateDisabled];
    [lastBtn setTitleColor:whiteColor forState:UIControlStateDisabled];
    [lastBtn setEnabled:NO];
    [lastBtn setSelected:NO];
    
    [buttons addObject:lastBtn];
    return buttons;
}

- (NSMutableDictionary*) getDividersDictionary {
    NSMutableDictionary* dividers = [[[NSMutableDictionary alloc] initWithCapacity:3] autorelease];    
    //1
    NSMutableDictionary* leftNormal = [[NSMutableDictionary alloc] initWithCapacity:2];
    [leftNormal setObject:[UIImage imageNamed:@"divider_sh.png"] forKey:[NSNumber numberWithUnsignedInteger:UIControlStateSelected]];
    [leftNormal setObject:[UIImage imageNamed:@"divider_sd.png"] forKey:[NSNumber numberWithUnsignedInteger:UIControlStateDisabled]];
    [dividers setObject:leftNormal forKey:[NSNumber numberWithUnsignedInteger:UIControlStateNormal]];
    [leftNormal release];
    
    //2
    NSMutableDictionary* leftDisabled = [[NSMutableDictionary alloc] initWithCapacity:2];
    [leftDisabled setObject:[UIImage imageNamed:@"divider_dd.png"] forKey:[NSNumber numberWithUnsignedInteger:UIControlStateDisabled]];
    [leftDisabled setObject:[UIImage imageNamed:@"divider_dh.png"] forKey:[NSNumber numberWithUnsignedInteger:UIControlStateSelected]];
    [dividers setObject:leftDisabled forKey:[NSNumber numberWithUnsignedInteger:UIControlStateDisabled]];
    [leftDisabled release];
    
    //3
    NSMutableDictionary* leftSelected = [[NSMutableDictionary alloc] initWithCapacity:1];
    [leftSelected setObject:[UIImage imageNamed:@"divider_hd.png"] forKey:[NSNumber numberWithUnsignedInteger:UIControlStateDisabled]];
    [dividers setObject:leftSelected forKey:[NSNumber numberWithUnsignedInt:UIControlStateSelected]];
    [leftSelected release];
    
    return dividers;
}

#pragma mark - Rotation

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

    [self setPhotoTitlesForInterfaceOrientation:toInterfaceOrientation];
}

- (void) setPhotoTitlesForInterfaceOrientation: (UIInterfaceOrientation) orientation {
    for (NSUInteger i=0; i<5; i++) {
        UIButton* btn = [self.customSegmentedControl.buttons objectAtIndex:i+1];
        NSString* title = [NSString stringWithFormat:((UIInterfaceOrientationIsPortrait(orientation)) ? @"%d" : @"Photo %d"), i+1];
        [btn setTitle:title forState:UIControlStateSelected];
        [btn setTitle:title forState:UIControlStateDisabled];
    }
}

#pragma mark - Photo taking process

- (void)selectedIndexChangedFrom:(NSUInteger)oldSegmentIndex to:(NSUInteger)newSegmentIndex setnder:(CustomizableSegmentedControl *)sender {
    if (oldSegmentIndex > 0) {
        UIButton* oldSelectedBtn = [sender.buttons objectAtIndex:oldSegmentIndex];
        oldSelectedBtn.enabled = NO;
        UIButton* newSelectedBtn = [sender.buttons objectAtIndex:newSegmentIndex];
        newSelectedBtn.enabled = YES;
    }
}

- (IBAction)takePhotos:(id)sender {
    [UIView animateWithDuration:0.2 animations:^{
        self.whenYouAreReadyLabel.alpha = 
        self.photosWillBeTakenLabel.alpha = 0;
       
        self.getReadyLabel.alpha = 1;
    }];
    
    self.button.hidden = YES;
    self.segmentedControl.selectedSegmentIndex++;
    
    [NSTimer scheduledTimerWithTimeInterval:TIMER_DELAY target:self selector:@selector(pictureN) userInfo:nil repeats:NO];
}

- (void)getReady {
    if(!self.captureSession.isRunning) {
        [self.captureSession startRunning];
    }
    self.getReadyLabel.text = GET_READY_MSG;
    self.customSegmentedControl.selectedSegmentIndex++;
    [NSTimer scheduledTimerWithTimeInterval:TIMER_DELAY target:self selector:@selector(pictureN) userInfo:nil repeats:NO];
}

- (void)pictureN {
    self.getReadyLabel.text = [NSString stringWithFormat: PHOTO_N_OF_5_MSG, self.photoNumber++];
    [NSTimer scheduledTimerWithTimeInterval:TIMER_DELAY target:self selector:@selector(countDown) userInfo:nil repeats:NO];
}

- (void)countDown{
    _photoPicker = [[PhotoPickerController alloc] init];
    [self.photoPicker setTimerDelay:2];
    [self presentModalViewController:self.photoPicker animated:NO];
    [self.captureSession stopRunning];
}

- (void)saveImageToPhotoAlbum 
{
    [self dismissModalViewControllerAnimated:NO];
    Photo* photo = [[Photo alloc] initWithImage: self.photoPicker.image];
    [self.rootController.review.photos addObject: photo];
    [photo release];
    self.photoPicker = nil;
    [_photoPicker release];
    if (self.photoNumber <= 5) {
        [self getReady];
    } else {
        [RootController switchToController:@"RootPhotoController" rootController:self.rootController];
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

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [self.rootController goHome];
}

- (UIViewController *) getCurrentController{
    return self.currentViewController;
}

- (void) setCurrentController:(UIViewController *)controller{
    self.currentViewController = controller;
}

@end
