#import "UIImageWithReview.h"
#import <AVFoundation/AVFoundation.h>

@interface UIImageWithReview ()
@property (nonatomic, retain) UIImageView* reviewBg;
@property (nonatomic, retain) UILabel* textView;
@end

@implementation UIImageWithReview
@synthesize reviewBg = _reviewBg;
@synthesize textView = _textView;

#pragma mark - Initialization

- (id)initWithFrame:(CGRect)frame image:(UIImage*)image andText:(NSString*)text
{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.layer.shadowColor = [UIColor blackColor].CGColor;
        self.layer.shadowOffset = CGSizeMake(5, 5);
        self.layer.shadowOpacity = 0.8;
        self.layer.shadowRadius = 10;
                
        _reviewBg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"karo_film.png"]];
        
        _textView = [[UILabel alloc] initWithFrame:CGRectMake(120, 0, 364, 48)];
        self.textView.backgroundColor = [UIColor clearColor];
        self.textView.textAlignment = UITextAlignmentCenter;
        self.textView.textColor = [UIColor whiteColor];
        self.textView.font = [UIFont boldSystemFontOfSize:14];
        self.textView.text = text;
        self.textView.numberOfLines = 2;
        self.textView.lineBreakMode = UILineBreakModeTailTruncation;
        
        [self.reviewBg addSubview:self.textView];
        [self addSubview:self.reviewBg];
        
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
    self.reviewBg.image = [self imageByCropping:[self imageByCropping:[UIImage imageNamed:@"karo_film.png"] toRect:CGRectMake(20, 0, photo.size.width, 58)] toRect:CGRectMake(0, 0, photo.size.width, 58)];
    self.reviewBg.frame = CGRectMake(0, 282, photo.size.width, 58);
    
    self.textView.frame = CGRectMake(100, 0, photo.size.width-105, 48);
}


- (UIImage*)imageByCropping:(UIImage *)imageToCrop toRect:(CGRect)rect
{
    //create a context to do our clipping in
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef currentContext = UIGraphicsGetCurrentContext();
    
    //create a rect with the size we want to crop the image to
    //the X and Y here are zero so we start at the beginning of our
    //newly created context
    CGRect clippedRect = CGRectMake(0, 0, rect.size.width, rect.size.height);
    CGContextClipToRect( currentContext, clippedRect);
    
    //create a rect equivalent to the full size of the image
    //offset the rect by the X and Y we want to start the crop
    //from in order to cut off anything before them
    CGRect drawRect = CGRectMake(rect.origin.x * -1,
                                 rect.origin.y * -1,
                                 imageToCrop.size.width,
                                 imageToCrop.size.height);
    
    //draw the image to our clipped context using our offset rect
    CGContextDrawImage(currentContext, drawRect, imageToCrop.CGImage);
    
    //pull the image from our cropped context
    UIImage *cropped = UIGraphicsGetImageFromCurrentImageContext();
    
    //pop the context to get back to the default
    UIGraphicsEndImageContext();
    
    //Note: this is autoreleased
    return cropped;
}

@end
