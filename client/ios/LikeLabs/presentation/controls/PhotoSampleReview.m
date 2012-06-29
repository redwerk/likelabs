#import "PhotoSampleReview.h"
#import <AVFoundation/AVFoundation.h>

@interface PhotoSampleReview()
@property (nonatomic, retain) UILabel* label;
@property (nonatomic, retain) UIImageView* imageView;
- (void) setPhoto: (UIImage *)photo;
@end

@implementation PhotoSampleReview
@synthesize label = _label;
@synthesize imageView = _imageView;

- (id)initWithText:(NSString*) text andPhoto:(UIImage*)photo
{
    self = [super initWithFrame:CGRectMake(0, 0, 420, 420)];
    if (self) {
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"promo_photo_bg.png"]];
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(15, 15, 390, 340)];
        [self setPhoto:photo];
        [self addSubview:self.imageView];
        
        _label = [[UILabel alloc] initWithFrame:CGRectMake(0, 355, 420, 65)];
        self.label.backgroundColor = [UIColor clearColor];
        self.label.textColor = [UIColor darkGrayColor];
        self.label.font = [UIFont fontWithName:@"BadScript-Regular.otf" size:20];
        self.label.textAlignment = UITextAlignmentCenter;
        self.label.text = text;
        self.label.numberOfLines = 2;
        self.label.lineBreakMode = UILineBreakModeTailTruncation;
        [self addSubview:self.label];
        
        self.clipsToBounds = NO;
        self.layer.shadowOpacity = 0.75;
        self.layer.shadowPath = [UIBezierPath bezierPathWithRect:self.bounds].CGPath;
        
        self.autoresizingMask = UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin;
    }
    return self;
}

- (void)dealloc {
    self.label = nil;
    self.imageView = nil;
    [super dealloc];
}

- (void) setPhoto: (UIImage *)photo {
    CGSize maxPhotoSize = CGSizeMake(390, 340);
    CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
    photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
    
    self.imageView.image = photo;
    
    CGPoint oldCenter = self.imageView.center;
    self.imageView.frame = CGRectMake(0, 0, photo.size.width, photo.size.height);
    self.imageView.center = oldCenter;
}



@end
