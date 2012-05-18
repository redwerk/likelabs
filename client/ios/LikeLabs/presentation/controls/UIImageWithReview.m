#import "UIImageWithReview.h"
#import <AVFoundation/AVFoundation.h>

@interface UIImageWithReview ()
@property (nonatomic, retain) UIImageView* reviewBg;
@property (nonatomic, retain) UITextView* textView;
@property (nonatomic, retain) UIView* maskView;
@end

@implementation UIImageWithReview
@synthesize reviewBg = _reviewBg;
@synthesize textView = _textView;
@synthesize maskView = _maskView;

#pragma mark - Initialization

- (id)initWithFrame:(CGRect)frame image:(UIImage*)image andText:(NSString*)text
{
    self = [super initWithFrame:frame];
    if (self) {
        _maskView = [[UIView alloc] initWithFrame:self.bounds];
        self.maskView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        self.maskView.backgroundColor = [UIColor clearColor];
        self.maskView.clipsToBounds = YES;
        [self addSubview:self.maskView];
        
        self.layer.shadowColor = [UIColor blackColor].CGColor;
        self.layer.shadowOffset = CGSizeMake(5, 5);
        self.layer.shadowOpacity = 0.8;
        self.layer.shadowRadius = 10;

        _reviewBg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"karo_film.png"]];
        self.reviewBg.frame = CGRectMake(-20, 282, 497, 58);
        
        _textView = [[UITextView alloc] initWithFrame:CGRectMake(120, 0, 364, 48)];
        self.textView.backgroundColor = [UIColor clearColor];
        self.textView.textAlignment = UITextAlignmentCenter;
        self.textView.textColor = [UIColor whiteColor];
        self.textView.font = [UIFont boldSystemFontOfSize:14];
        self.textView.text = text;
        
        [self.reviewBg addSubview:self.textView];
        [self.maskView addSubview:self.reviewBg];
        
        [self setPhoto:image];
    }
    return self;
}

#pragma mark - Memory management

- (void)dealloc {
    self.reviewBg = nil;
    self.textView = nil;
    [_reviewBg release];
    [_textView release];
    [super dealloc];
}

#pragma mark - Photo management

- (void) setPhoto: (UIImage *)photo {
    CGSize maxPhotoSize = CGSizeMake(468, 350);
    CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
    photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
    
    self.image = photo;
    
    CGPoint oldCenter = self.center;
    self.frame = CGRectMake(0, 0, photo.size.width, photo.size.height);
    self.center = oldCenter;
    
    self.textView.frame = CGRectMake(120, 0, photo.size.width-120, 48);
}

@end
