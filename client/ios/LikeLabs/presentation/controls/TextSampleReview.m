#import "TextSampleReview.h"
#import <AVFoundation/AVFoundation.h>

@implementation TextSampleReview

- (id)initWithBackgroundColor: (ReviewBackgroundColor) backgroundColor andText: (NSString*) text
{    
    self = [super initWithFrame:CGRectMake(0, 0, 420, 148)];
    if (self) {        
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:(backgroundColor == ReviewBackgroundColorBlue) ? @"promo_bg_blue.png" : @"promo_bg_yellow.png"]];
        self.textColor = [UIColor darkGrayColor];
        self.font = [UIFont fontWithName:@"BadScript-Regular.otf" size:20];
        self.textAlignment = UITextAlignmentCenter;
        self.text = text;
        
        self.layer.shadowColor = [UIColor blackColor].CGColor;
        self.layer.shadowOffset = CGSizeMake(0, 6);
        self.layer.shadowRadius = 21;
    }
    return self;
}

@end