#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface TakePhotoController : UIViewController <ChildController, UIImagePickerControllerDelegate, UINavigationControllerDelegate>
@property (retain, nonatomic) IBOutlet UIView *headerView;
@end
