#import <AVFoundation/AVFoundation.h>
#import "PhotoMessageController.h"
#import "RootController.h"
#import "RootPhotoController.h"
#import "Review.h"

@interface PhotoMessageController ()
@property (nonatomic,retain) UIViewController <ContainerController> *rootController;
@property (nonatomic, assign) BOOL textPlaceholderActive;
@property (nonatomic, assign) Review* review;

- (void) setPhoto: (UIImage *)photo ;
@end

@implementation PhotoMessageController
@synthesize textView = _textView;
@synthesize imageView = _imageView;
@synthesize messageView = _messageView;
@synthesize review = _review;

static NSString *const bgLandscape = @"bg_landscape.png";
static NSString *const bgPortrait = @"bg_portrait.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
static NSString *const GREETING = @"Start typing a message!";

@synthesize rootController = _rootController;
@synthesize textPlaceholderActive = _textPlaceholderActive;


#pragma mark - Initialization
-(id)initWithRootController:(UIViewController <ContainerController> *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.review = [self.rootController getReview];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOffset = CGSizeMake(5, 5);
    self.imageView.layer.shadowOpacity = 0.8;
    self.imageView.layer.shadowRadius = 10;
    
    self.textView.layer.borderColor = [UIColor blackColor].CGColor;
    self.textView.layer.borderWidth = 1;
    self.textView.layer.cornerRadius = 10;
    
    if (self.review.text && self.review.text.length) {
        self.textView.text = self.review.text;
        self.textView.textColor = [UIColor blackColor];
    } else {
        self.textPlaceholderActive = YES;
        self.textView.text = GREETING;
        self.textView.textColor = [UIColor lightGrayColor];
    }
    [self.textView becomeFirstResponder];    
    [self setPhoto:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image];
    [self willAnimateRotationToInterfaceOrientation:[self interfaceOrientation] duration:0];
}

- (BOOL) resignFirstResponder {
    return [self.textView resignFirstResponder ];
}

#pragma mark - Review management

- (void) setPhoto: (UIImage *)photo {
    CGSize maxPhotoSize = CGSizeMake(409, 296);
    CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
    photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
    
    self.imageView.image = photo;
    
    //CGPoint oldCenter = self.imageView.center;
    self.imageView.frame = CGRectMake(0, 0, photo.size.width, photo.size.height);
    //self.imageView.center = oldCenter;
}

# pragma mark - Memory management

- (void)viewDidUnload
{
    [self setTextView:nil];
    [self setImageView:nil];
    [self setMessageView:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    self.rootController = nil;
    [_textView release];
    [_imageView release];
    [_messageView release];
    [super dealloc];
}

#pragma mark - TextViewDelegate implementation

- (BOOL)textView:(UITextView *)view shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (self.textPlaceholderActive) {
        self.textView.text = @"";
        self.textView.textColor = [UIColor blackColor];
        self.textPlaceholderActive = false;
    } 
    return YES;
}

- (void)textViewDidChange:(UITextView* ) view {
    if (self.textView.text.length == 0) {
        self.textView.textColor = [UIColor lightGrayColor];
        self.textView.text = GREETING;
        self.textPlaceholderActive = true;
    }
    self.review.text = (self.textPlaceholderActive) ? @"" : self.textView.text;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
    UIColor *background;
    if(UIInterfaceOrientationIsPortrait(toInterfaceOrientation)){
        background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:bgPortrait]];
        self.messageView.center = CGPointMake(384, 600);
        self.imageView.center = CGPointMake(384, 259);        
    } else {
        background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:bgLandscape]];
        self.imageView.center = CGPointMake(224, 259);
        self.messageView.center = CGPointMake(726, 259);
    }
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)next:(id)sender {
    [self.rootController step];
}
@end
