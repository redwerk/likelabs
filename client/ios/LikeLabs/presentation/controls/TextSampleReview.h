#import <UIKit/UIKit.h>

typedef enum {
    ReviewBackgroundColorYellow,
    ReviewBackgroundColorBlue
} ReviewBackgroundColor;

@interface TextSampleReview : UILabel
- (id)initWithBackgroundColor: (ReviewBackgroundColor) backgroundColor andText: (NSString*) text;
@end
