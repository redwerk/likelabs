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
@end

@implementation PhotoPickerController

static const float PADDING = 20;
static const int COUNTER_LENGTH = 3;
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
    _timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(onTimer) userInfo:nil repeats:YES];
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
    self.image = nil;
    [_captureSession release];
    [_previewLayer release];
    [_img release];
    [_label release];
    [_imageOutput release];
    [_image release];
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
    [self.captureSession setSessionPreset:AVCaptureSessionPresetMedium];
    
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
    videoConnection.videoOrientation = [UIApplication sharedApplication].statusBarOrientation;
	NSLog(@"about to request a capture from: %@", self.imageOutput);
	[self.imageOutput captureStillImageAsynchronouslyFromConnection:videoConnection 
                                                         completionHandler:^(CMSampleBufferRef imageSampleBuffer, NSError *error) { 
                                                             CFDictionaryRef exifAttachments = CMGetAttachment(imageSampleBuffer, kCGImagePropertyExifDictionary, NULL);
                                                             if (exifAttachments) {
                                                                 NSLog(@"attachements: %@", exifAttachments);
                                                             } else { 
                                                                 NSLog(@"no attachments");
                                                             }
                                                             NSData *imageData = [AVCaptureStillImageOutput jpegStillImageNSDataRepresentation:imageSampleBuffer];    
                                                             _image = [[UIImage alloc] initWithData:imageData];
                                                             
                                                             [[NSNotificationCenter defaultCenter] postNotificationName:kImageCapturedSuccessfully object:nil];
                                                         }];
}

@end
