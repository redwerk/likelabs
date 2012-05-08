#import <MobileCoreServices/MobileCoreServices.h>
#import <AVFoundation/AVFoundation.h>
#import "TakePhotoController.h"
#import "PhotosController.h"

#define OVERLAY_ALPHA 0.90f

@interface TakePhotoController()
@property (nonatomic, retain) UIViewController<RootControllerProtocol>* rootController;
@property (nonatomic, retain) AVCaptureSession *captureSession;
@property (nonatomic, retain) UIImageView *imageView;
@property (nonatomic, retain) CALayer *customLayer;
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *prevLayer;

@end

@implementation TakePhotoController
@synthesize button = _button;
@synthesize headerView = _headerView;
@synthesize rootController = _rootController;
@synthesize captureSession = _captureSession;
@synthesize imageView = _imageView;
@synthesize customLayer = _customLayer;
@synthesize prevLayer = _prevLayer;

- (id)initWithRootController:(UIViewController<RootControllerProtocol> *)rootController {
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

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];    
    [self.view bringSubviewToFront:self.headerView];
    [self.view bringSubviewToFront:self.button];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth];
    self.headerView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"instruction_banner_bg.png"]];
    [self initCapture];    
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
    
    
	/*We setupt the output*/
	AVCaptureVideoDataOutput *captureOutput = [[AVCaptureVideoDataOutput alloc] init];
	/*While a frame is processes in -captureOutput:didOutputSampleBuffer:fromConnection: delegate methods no other frames are added in the queue.
	 If you don't want this behaviour set the property to NO */
	captureOutput.alwaysDiscardsLateVideoFrames = YES; 
	
	/*We create a serial queue to handle the processing of our frames*/
	dispatch_queue_t queue;
	queue = dispatch_queue_create("cameraQueue", NULL);
	[captureOutput setSampleBufferDelegate:self queue:queue];
	dispatch_release(queue);
    
	// Set the video output to store frame in BGRA (It is supposed to be faster)
	NSString* key = (NSString*)kCVPixelBufferPixelFormatTypeKey; 
	NSNumber* value = [NSNumber numberWithUnsignedInt:kCVPixelFormatType_32BGRA]; 
	NSDictionary* videoSettings = [NSDictionary dictionaryWithObject:value forKey:key]; 
	[captureOutput setVideoSettings:videoSettings]; 
    
	self.captureSession = [[AVCaptureSession alloc] init];
	[self.captureSession addInput:captureInput];
	[self.captureSession addOutput:captureOutput];
    
    [self.captureSession setSessionPreset:AVCaptureSessionPresetMedium];
    
	self.prevLayer = [AVCaptureVideoPreviewLayer layerWithSession: self.captureSession];
	self.prevLayer.frame = self.view.bounds;
    self.prevLayer.orientation = [UIApplication sharedApplication].statusBarOrientation;
	self.prevLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
	[self.view.layer addSublayer: self.prevLayer];
    
	[self.captureSession startRunning];	
}

#pragma mark -
#pragma mark AVCaptureSession delegate
- (void)captureOutput:(AVCaptureOutput *)captureOutput 
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer 
	   fromConnection:(AVCaptureConnection *)connection 
{ 
//	/*We create an autorelease pool because as we are not in the main_queue our code is
//	 not executed in the main thread. So we have to create an autorelease pool for the thread we are in*/
//	
//	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
//	
//    CVImageBufferRef imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer); 
//    /*Lock the image buffer*/
//    CVPixelBufferLockBaseAddress(imageBuffer,0); 
//    /*Get information about the image*/
//    uint8_t *baseAddress = (uint8_t *)CVPixelBufferGetBaseAddress(imageBuffer); 
//    size_t bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer); 
//    size_t width = CVPixelBufferGetWidth(imageBuffer); 
//    size_t height = CVPixelBufferGetHeight(imageBuffer);  
//    
//    /*Create a CGImageRef from the CVImageBufferRef*/
//    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB(); 
//    CGContextRef newContext = CGBitmapContextCreate(baseAddress, width, height, 8, bytesPerRow, colorSpace, kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedFirst);
//    CGImageRef newImage = CGBitmapContextCreateImage(newContext); 
//	
//    /*We release some components*/
//    CGContextRelease(newContext); 
//    CGColorSpaceRelease(colorSpace);
//    
//    /*We display the result on the custom layer. All the display stuff must be done in the main thread because
//	 UIKit is no thread safe, and as we are not in the main thread (remember we didn't use the main_queue)
//	 we use performSelectorOnMainThread to call our CALayer and tell it to display the CGImage.*/
//	[self.customLayer performSelectorOnMainThread:@selector(setContents:) withObject: (id) newImage waitUntilDone:YES];
//	
//	/*We display the result on the image view (We need to change the orientation of the image so that the video is displayed correctly).
//	 Same thing as for the CALayer we are not in the main thread so ...*/
//	UIImage *image= [UIImage imageWithCGImage:newImage scale:1.0 orientation:UIImageOrientationRight];
//	
//	/*We relase the CGImageRef*/
//	CGImageRelease(newImage);
//	
//	[self.imageView performSelectorOnMainThread:@selector(setImage:) withObject:image waitUntilDone:YES];
//	
//	/*We unlock the  image buffer*/
//	CVPixelBufferUnlockBaseAddress(imageBuffer,0);
//	
//	[pool drain];
} 


- (void)viewDidUnload
{
    [self setRootController:nil];
    [self setHeaderView:nil];
    [self setButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

-(void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    self.prevLayer.orientation = toInterfaceOrientation;
    
    CGSize viewSize = self.view.bounds.size;
    CGFloat viewOffset = self.view.frame.origin.y;
    self.prevLayer.frame = CGRectMake(0, 0, viewSize.height + viewOffset , viewSize.width - viewOffset);
//    self.button.center = CGPointMake(self.button.center.y, self.button.center.x);
}

- (void)dealloc {
    [_headerView release];
    [_rootController release];
    [_prevLayer release];
    [_customLayer release];
    [_captureSession release];
    [_imageView release];
    [_button release];
    [super dealloc];
}
@end
