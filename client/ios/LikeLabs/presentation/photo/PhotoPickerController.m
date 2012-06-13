#import <AVFoundation/AVFoundation.h>
#import <ImageIO/ImageIO.h>
#import "PhotoPickerController.h"
#import "Photo.h"
#import "Review.h"
#import "RootController.h"

@interface PhotoPickerController ()
@property (nonatomic, retain) RootController *rootController;
@property (nonatomic, assign) Review *review;
@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, retain) NSTimer *timer;
@property (nonatomic, retain) AVCaptureStillImageOutput *imageOutput;
@property (assign) NSInteger seconds;

- (void) initCapture;
- (void) captureImage;
- (UIImage *)scaleAndRotateImage:(UIImage *)image;
- (void) countDown;
- (void) onTimer;

@end

@implementation PhotoPickerController

static const float PADDING = 20;
static const int COUNTER_LENGTH = 3;
static const float COUNTER_INTERVAL = 1;
static const float PHOTO_INTERVAL = 2;
static const float WHITE_SCREEN_DURATION = 0.2;
NSString *const FIRST_MSG = @"When you are ready click the photo icon to start the camera. 5 photos will be taken with a 2 second interval.";
NSString *const GET_READY_MSG = @"Get Ready!";

@synthesize rootController = _rootController;
@synthesize review = _review;
@synthesize label = _label;
@synthesize captureSession = _captureSession;
@synthesize previewLayer = _previewLayer;
@synthesize timer = _timer;
@synthesize imageOutput = _imageOutput;
@synthesize messageView = _messageView;
@synthesize messageLabel = _messageLabel;
@synthesize startButton = _startButton;
@synthesize seconds = _seconds;

- (id)initWithRootController:(RootController *) rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.rootController.review = [[[Review alloc] initWithReviewType:ReviewTypePhoto] autorelease];
        self.review = self.rootController.review;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self initCapture];
    
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    
    [self.view bringSubviewToFront:self.startButton];
    
    self.messageView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"instruction_banner_bg.png"]];
    [self.view bringSubviewToFront:self.messageView];
    
    [self.view bringSubviewToFront:self.messageLabel];
    self.messageLabel.text = FIRST_MSG;
    
}

- (void)viewDidLayoutSubviews {
    [self willRotateToInterfaceOrientation:self.interfaceOrientation duration:0];
}

- (void)viewDidUnload
{
    [self setRootController:nil];
    [self setCaptureSession:nil];
    [self setPreviewLayer:nil];
    [self setLabel:nil];
    [self setTimer:nil];
    [self setImageOutput:nil];
    [self setMessageView:nil];
    [self setMessageLabel:nil];
    [self setStartButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [_rootController release];
    [_captureSession release];
    [_previewLayer release];
    [_label release];
    [_imageOutput release];
    [_messageView release];
    [_messageLabel release];
    [_startButton release];
    [super dealloc];
}

- (IBAction)start:(id)sender {
    self.startButton.hidden = YES;
    self.messageLabel.text = GET_READY_MSG;
    
    [self.view bringSubviewToFront:self.label];
    self.seconds = COUNTER_LENGTH;
    self.timer = [NSTimer scheduledTimerWithTimeInterval:COUNTER_INTERVAL target:self selector:@selector(countDown) userInfo:nil repeats:YES];
}

- (void) countDown {
    self.messageLabel.hidden = YES;
    self.messageView.hidden = YES;
    self.label.text = [NSString stringWithFormat:@"%d", self.seconds];
    if (self.seconds == 0) {
        [self.timer invalidate];
        self.timer = nil;
        self.label.hidden = YES;
        self.seconds = 5;
        [self onTimer];
    } else {
        self.label.hidden = NO;
        self.seconds--;
    }
}

- (void) onTimer {
    if (self.seconds == 0) {
        [self.timer invalidate];
        self.timer = nil;
        [RootController switchToController:@"RootPhotoController" rootController:self.rootController];
    } else {
        [self captureImage];
        [UIView animateWithDuration:WHITE_SCREEN_DURATION/2 delay:0 options:UIViewAnimationOptionCurveLinear animations:^{
            self.view.alpha = 0;
        } completion:^(BOOL finished){
            [UIView animateWithDuration:WHITE_SCREEN_DURATION/2 delay:WHITE_SCREEN_DURATION/2 options:UIViewAnimationOptionCurveLinear animations:^{
                self.view.alpha = 1;
            } completion:^(BOOL finished){
                
            }];
        }];
        self.seconds--;
        self.timer = [NSTimer scheduledTimerWithTimeInterval:PHOTO_INTERVAL target:self selector:@selector(onTimer) userInfo:nil repeats:NO];
    }

}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {

    CGSize viewSize = self.view.frame.size;
    self.label.center = CGPointMake(viewSize.width/2, viewSize.height/2);
    self.startButton.center = CGPointMake(viewSize.width/2, viewSize.height-184);
    self.messageLabel.center = CGPointMake(viewSize.width/2, 40);
    self.previewLayer.frame = self.view.frame;
    self.previewLayer.orientation = toInterfaceOrientation;    
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
    
    NSDictionary *outputSettings = [[NSDictionary alloc] initWithObjectsAndKeys:AVVideoCodecJPEG,AVVideoCodecKey,nil];    
    _imageOutput = [[AVCaptureStillImageOutput alloc] init];
    self.imageOutput.outputSettings = outputSettings;
    [outputSettings release];
    
	_captureSession = [[AVCaptureSession alloc] init];
	[self.captureSession addInput:captureInput];
    [self.captureSession addOutput:self.imageOutput];    
    [self.captureSession setSessionPreset:AVCaptureSessionPresetPhoto];
    
	self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession: self.captureSession];
	self.previewLayer.frame = self.view.frame;
    self.previewLayer.orientation = [UIApplication sharedApplication].statusBarOrientation;
	self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
	[self.view.layer addSublayer: self.previewLayer];
	[self.captureSession startRunning];	
}

- (void)captureImage {  
	AVCaptureConnection *videoConnection = nil;
	for (AVCaptureConnection *connection in self.imageOutput.connections) {
		for (AVCaptureInputPort *port in [connection inputPorts]) {
			if ([[port mediaType] isEqual:AVMediaTypeVideo]) {
				videoConnection = connection;
				break;
			}
		}
		if (videoConnection) { 
            break; 
        }
	}
	[self.imageOutput captureStillImageAsynchronouslyFromConnection:videoConnection 
         completionHandler:^(CMSampleBufferRef imageSampleBuffer, NSError *error) { 
             NSData *imageData = [AVCaptureStillImageOutput jpegStillImageNSDataRepresentation:imageSampleBuffer];    
             UIImageOrientation io;
             switch (self.interfaceOrientation) {
                 case UIInterfaceOrientationPortrait:
                     io = UIImageOrientationRight;
                     break;
                 case UIInterfaceOrientationLandscapeLeft:
                     io = UIImageOrientationUp;
                     break;
                 case UIInterfaceOrientationLandscapeRight:
                     io = UIImageOrientationDown;
                     break;
                 case UIInterfaceOrientationPortraitUpsideDown:
                     io = UIImageOrientationLeft;
                     break;
             }
             UIImage* tmp = [UIImage imageWithCGImage:[[[UIImage alloc] initWithData:imageData] autorelease].CGImage scale:1.0 orientation:io];
             Photo* photo = [[Photo alloc] initWithImage: [self scaleAndRotateImage:tmp]];

             [self.review.photos addObject: photo];
             [photo release];
         }];
}

- (UIImage *)scaleAndRotateImage:(UIImage *)image {
    CGImageRef imgRef = image.CGImage;
    
    CGFloat width = CGImageGetWidth(imgRef);
    CGFloat height = CGImageGetHeight(imgRef);
    
    CGAffineTransform transform = CGAffineTransformIdentity;
    CGRect bounds = CGRectMake(0, 0, width, height);
    CGFloat scaleRatio = bounds.size.width / width;
    CGSize imageSize = CGSizeMake(CGImageGetWidth(imgRef), CGImageGetHeight(imgRef));
    CGFloat boundHeight;
    UIImageOrientation orient = image.imageOrientation;
    switch(orient) {
            
            case UIImageOrientationUp: //EXIF = 1
            transform = CGAffineTransformIdentity;
            break;
            
            case UIImageOrientationUpMirrored: //EXIF = 2
            transform = CGAffineTransformMakeTranslation(imageSize.width, 0.0);
            transform = CGAffineTransformScale(transform, -1.0, 1.0);
            break;
            
            case UIImageOrientationDown: //EXIF = 3
            transform = CGAffineTransformMakeTranslation(imageSize.width, imageSize.height);
            transform = CGAffineTransformRotate(transform, M_PI);
            break;
            
            case UIImageOrientationDownMirrored: //EXIF = 4
            transform = CGAffineTransformMakeTranslation(0.0, imageSize.height);
            transform = CGAffineTransformScale(transform, 1.0, -1.0);
            break;
            
            case UIImageOrientationLeftMirrored: //EXIF = 5
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(imageSize.height, imageSize.width);
            transform = CGAffineTransformScale(transform, -1.0, 1.0);
            transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
            break;
            
            case UIImageOrientationLeft: //EXIF = 6
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(0.0, imageSize.width);
            transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
            break;
            
            case UIImageOrientationRightMirrored: //EXIF = 7
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeScale(-1.0, 1.0);
            transform = CGAffineTransformRotate(transform, M_PI / 2.0);
            break;
            
            case UIImageOrientationRight: //EXIF = 8
            boundHeight = bounds.size.height;
            bounds.size.height = bounds.size.width;
            bounds.size.width = boundHeight;
            transform = CGAffineTransformMakeTranslation(imageSize.height, 0.0);
            transform = CGAffineTransformRotate(transform, M_PI / 2.0);
            break;
            
            default:
            [NSException raise:NSInternalInconsistencyException format:@"Invalid image orientation"];
            
        }
    
    UIGraphicsBeginImageContext(bounds.size);
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    if (orient == UIImageOrientationRight || orient == UIImageOrientationLeft) {
        CGContextScaleCTM(context, -scaleRatio, scaleRatio);
        CGContextTranslateCTM(context, -height, 0);
        }
    else {
        CGContextScaleCTM(context, scaleRatio, -scaleRatio);
        CGContextTranslateCTM(context, 0, -height);
        }
    
    CGContextConcatCTM(context, transform);
    
    CGContextDrawImage(UIGraphicsGetCurrentContext(), CGRectMake(0, 0, width, height), imgRef);
    UIImage *imageCopy = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return imageCopy;
}

- (UIViewController *) getCurrentController{
    return self;
}

- (void) setCurrentController:(UIViewController *)controller{
    
}

@end
