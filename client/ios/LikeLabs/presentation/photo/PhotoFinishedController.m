#import "PhotoFinishedController.h"
#import "RootPhotoController.h"
#import "UIImageWithReview.h"
#import <AVFoundation/AVFoundation.h>

@interface PhotoFinishedController ()
@property (nonatomic, retain) RootPhotoController* rootController;
@property (nonatomic, assign) Review* review;
@property (nonatomic, retain) UIImageWithReview* imageView;
@end

@implementation PhotoFinishedController
@synthesize button = _button;
@synthesize rootController = _rootController;
@synthesize imageView = _imageView;
@synthesize review = _review;

#pragma mark - Initialization
-(id)initWithRootController:(RootPhotoController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.review = self.rootController.rootController.review;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    
    _imageView = [[UIImageWithReview alloc] initWithFrame:CGRectMake(24, 211, 468, 350) image:[self.review.photos objectAtIndex:self.review.reviewPhotoIndex] andText:self.review.text];    
    [self.view addSubview:self.imageView];
    
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setButton:nil];
    [super viewDidUnload];
    self.rootController = nil;
    self.imageView = nil;
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_button release];
    [super dealloc];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation)orientation {
    [self setBackgroundForInterfaceOrientation:orientation];
    if (UIInterfaceOrientationIsLandscape(orientation)) {
        //LANDSCAPE
        self.imageView.frame = CGRectMake(24, 211, 468, 350);
        self.button.frame = CGRectMake(552, 387, 422,86);
    } else {
        //PORTRAIT
        self.imageView.frame = CGRectMake(142, 275, 468, 350);
        self.button.frame = CGRectMake(47, 895, 667,86);
    }
    [self.imageView setPhoto:[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]];
}

- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation {
    UIColor *background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:UIInterfaceOrientationIsLandscape(orientation) ? @"photo_thankyou_bg_landscape.png" : @"photo_thankyou_bg_portrait.png"]];
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [self.rootController goHome:sender];
}
@end
