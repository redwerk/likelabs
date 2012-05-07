#import <MobileCoreServices/MobileCoreServices.h>
#import "TakePhotoController.h"
#import "PhotosController.h"

@interface TakePhotoController()
@property (nonatomic, retain) UIViewController<RootControllerProtocol>* rootController;
@end

@implementation TakePhotoController
@synthesize headerView = _headerView;
@synthesize rootController = _rootController;

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

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth];

    UIImagePickerControllerSourceType sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        sourceType = UIImagePickerControllerSourceTypeCamera;
    }
    UIImagePickerController* picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.sourceType = sourceType;
    picker.mediaTypes = [NSArray arrayWithObjects:(NSString*)kUTTypeImage, nil];
    picker.allowsEditing = NO;
    picker.showsCameraControls = NO;
    picker.cameraOverlayView = self.headerView;
    picker.cameraDevice = UIImagePickerControllerCameraDeviceFront;
    [self presentViewController:picker animated:YES completion:^{}];
    [picker release];
}

- (void)viewDidUnload
{
    [self setRootController:nil];
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

- (void)dealloc {
    [_headerView release];
    [_rootController release];
}

@end
