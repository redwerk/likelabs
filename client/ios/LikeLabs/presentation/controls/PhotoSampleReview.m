#import "PhotoSampleReview.h"

@interface PhotoSampleReview()
@property (nonatomic, retain) UILabel* label;
@property (nonatomic, retain) UIImageView* imageView;
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
        self.imageView.image = photo;
        [self addSubview:self.imageView];
        
        _label = [[UILabel alloc] initWithFrame:CGRectMake(0, 355, 420, 65)];
        self.label.backgroundColor = [UIColor clearColor];
        self.label.textColor = [UIColor darkGrayColor];
        self.label.font = [UIFont fontWithName:@"BadScript-Regular.otf" size:20];
        self.label.textAlignment = UITextAlignmentCenter;
        self.label.text = text;
        [self addSubview:self.label];
    }
    return self;
}

- (void)dealloc {
    self.label = nil;
    self.imageView = nil;
    [super dealloc];
}


@end
