#import <UIKit/UIKit.h>

@interface PhotoPickerController : UIViewController 

extern NSString *const kImageCapturedSuccessfully;

@property (retain, nonatomic) IBOutlet UILabel *label;
@property (retain, nonatomic) IBOutlet UIImageView *img;
@property (retain, nonatomic) UIImage *image;

- (void)setTimerDelay:(CGFloat) delay;
@end
