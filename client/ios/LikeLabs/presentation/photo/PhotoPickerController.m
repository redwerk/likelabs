#import <AVFoundation/AVFoundation.h>
#import <ImageIO/ImageIO.h>
#import "PhotoPickerController.h"

@interface PhotoPickerController ()
@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, retain) NSTimer *timer;
@property (nonatomic, retain) AVCaptureStillImageOutput *imageOutput;
@property (assign) NSInteger seconds;
- (void) initCapture;
- (void) captureImage;
- (UIImage *)scaleAndRotateImage:(UIImage *)image;
@end

@implementation PhotoPickerController

static const float PADDING = 20;
static const int COUNTER_LENGTH = 2;
static const float TIMER_INTERVAL = 0.2;
NSString *const kImageCapturedSuccessfully = @"ImageCapturedSuccessfully";

@synthesize label = _label;
@synthesize img = _img;
@synthesize captureSession = _captureSession;
@synthesize previewLayer = _previewLayer;
@synthesize timer = _timer;
@synthesize imageOutput = _imageOutput;
@synthesize image = _image;
@synthesize seconds = _seconds;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)viewDidLayoutSubviews {
    [self willRotateToInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation duration:0];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    [self initCapture];
    [self.view bringSubviewToFront:self.img];
    [self.view bringSubviewToFront:self.label];
    self.seconds = COUNTER_LENGTH;
    _timer = [NSTimer scheduledTimerWithTimeInterval:TIMER_INTERVAL target:self selector:@selector(onTimer) userInfo:nil repeats:YES];
}

- (void) onTimer {
    self.seconds--;
    if (self.seconds == 0) {
        [self.timer invalidate];
        [self captureImage];
    } else {
        self.label.text = [NSString stringWithFormat:@"%d", self.seconds];
    }
}

- (void)viewDidUnload
{
    [self setCaptureSession:nil];
    [self setPreviewLayer:nil];
    [self setImg:nil];
    [self setLabel:nil];
    [self setTimer:nil];
    [self setImageOutput:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
//    self.image = nil;
    [_captureSession release];
    [_previewLayer release];
    [_img release];
    [_label release];
    [_imageOutput release];
//    [_image release];
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    self.previewLayer.orientation = toInterfaceOrientation;    
    CGSize viewSize = self.view.frame.size;
    switch (toInterfaceOrientation) {
        case UIInterfaceOrientationPortrait:
        case UIInterfaceOrientationPortraitUpsideDown:
            self.previewLayer.frame = CGRectMake(0, 0, viewSize.width, viewSize.height);
            break;           
        default:
            self.previewLayer.frame = CGRectMake(0, 0, viewSize.height, viewSize.width);
            break;
    }
    
    
    CGSize imgSize = self.img.frame.size;
    CGSize labelSize = self.label.frame.size;
    switch (toInterfaceOrientation) {
        case UIInterfaceOrientationLandscapeLeft:
            self.img.center = CGPointMake(viewSize.height - imgSize.width/2 - PADDING, viewSize.width/2 - imgSize.height/2);
            self.label.center = CGPointMake(viewSize.height - labelSize.width/2 - PADDING, viewSize.width/2 + labelSize.height/2);
            break;
        case UIInterfaceOrientationLandscapeRight:
            self.img.center = CGPointMake(imgSize.width/2 + PADDING, viewSize.width/2 - imgSize.height/2);
            self.label.center = CGPointMake(labelSize.width/2 + PADDING, viewSize.width/2 + labelSize.height/2);
            break;
        case UIInterfaceOrientationPortrait:
            self.img.center = CGPointMake(viewSize.width/2 - imgSize.width/2 + PADDING, imgSize.height/2 + PADDING);
            self.label.center = CGPointMake(viewSize.width/2 + labelSize.width/2 - PADDING, labelSize.height/2 + PADDING);
            break;
        case UIInterfaceOrientationPortraitUpsideDown:
            self.img.center = CGPointMake(viewSize.width/2 - imgSize.width/2 + PADDING, viewSize.height - imgSize.height/2 - PADDING);
            self.label.center = CGPointMake(viewSize.width/2 + labelSize.width/2 - PADDING, viewSize.height - imgSize.height/2 - PADDING);
            break;
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
    
    NSDictionary *outputSettings = [[NSDictionary alloc] initWithObjectsAndKeys:AVVideoCodecJPEG,AVVideoCodecKey,nil];    
    _imageOutput = [[AVCaptureStillImageOutput alloc] init];
    self.imageOutput.outputSettings = outputSettings;
    [outputSettings release];
    
	_captureSession = [[AVCaptureSession alloc] init];
	[self.captureSession addInput:captureInput];
    [self.captureSession addOutput:self.imageOutput];    
    [self.captureSession setSessionPreset:AVCaptureSessionPresetHigh];
    
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
                                                             UIImage* tmp = [UIImage imageWithCGImage:[[UIImage alloc] initWithData:imageData].CGImage scale:1.0 orientation:io];
                                                             _image = [self scaleAndRotateImage:tmp];
                                                             
                                                             [[NSNotificationCenter defaultCenter] postNotificationName:kImageCapturedSuccessfully object:nil];
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


@end
