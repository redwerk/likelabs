#import <UIKit/UIKit.h>

@interface UIImageWithReview : UIImageView

- (id)initWithFrame:(CGRect)frame image:(UIImage*)image andText:(NSString*)text;
- (void) setPhoto: (UIImage *)photo;

@end
