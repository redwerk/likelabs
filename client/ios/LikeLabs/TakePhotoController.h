#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface TakePhotoController : UIViewController <ChildController, AVCaptureVideoDataOutputSampleBufferDelegate>
@property (retain, nonatomic) IBOutlet UIButton *button;
@property (retain, nonatomic) IBOutlet UIView *headerView;
@end
