#import "SplashScreenController.h"
#import "RootController.h"
#import "SettingsDao.h"
#import "Review.h"
#import "TextSampleReview.h"
#import "PhotoSampleReview.h"
#import <AVFoundation/AVFoundation.h>

@interface SplashScreenController()
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) SettingsDao* dao;
@property (retain, nonatomic) NSArray* reviews;
@property (retain, nonatomic) NSTimer* timer;
@property (assign, nonatomic) NSUInteger reviewIndex;
@property (assign, nonatomic) BOOL pauseTimer;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (NSArray*) getReviewsLayouts: (NSArray*) reviews;
- (void) showNextReview;
- (NSUInteger) nextReviewIndex;
- (void) setLogo: (UIImage *)logo;
@end;

@implementation SplashScreenController

static CGFloat const TIMER_INTERVAL = 1;
static CGFloat const REVIEW_SPEED = 500; //pixel / second
static CGFloat const MAX_ANGLE_PORTRAIT = 12;
static CGFloat const MAX_ANGLE_LANDSCAPE = 40;

@synthesize startBtn = _startBtn;
@synthesize socialButtonsView = _socialButtonsView;
@synthesize shareYourSmileImg = _shareYourSmileImg;
@synthesize companyLogo = _companyLogo;
@synthesize reviewBox = _reviewBox;
@synthesize dao = _dao;
@synthesize reviews = _reviews;
@synthesize timer = _timer;
@synthesize reviewIndex = _reviewIndex;
@synthesize pauseTimer = _pauseTimer;

@synthesize rootController = _rootController;

#pragma mark - View lifecycle

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        _dao = [[SettingsDao alloc] init];
        self.reviews = [self getReviewsLayouts: self.dao.promoReviews];
        self.reviewIndex = 0;
        self.pauseTimer = NO;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self setLogo:self.dao.logo];
//    self.reviewBox.layer.borderColor = [UIColor whiteColor].CGColor;
//    self.reviewBox.layer.borderWidth = 1;
    [self layoutSubviewsForInterfaceOrientation:self.interfaceOrientation];
}

- (void)viewDidAppear:(BOOL)animated {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:TIMER_INTERVAL target:self selector:@selector(showNextReview) userInfo:nil repeats:YES];
    [self showNextReview];
}

- (void)viewDidDisappear:(BOOL)animated {
    [self.timer invalidate];
}

- (void)viewDidUnload
{
    [self setStartBtn:nil];
    [self setSocialButtonsView:nil];
    [self setShareYourSmileImg:nil];
    [self setCompanyLogo:nil];
    [self setDao:nil];
    [self setReviews:nil];
    [self setReviewBox:nil];
    [super viewDidUnload];
    self.rootController = nil;
}

- (void)dealloc {
    [_rootController release];
    [_startBtn release];
    [_socialButtonsView release];
    [_shareYourSmileImg release];
    [_companyLogo release];
    [_dao release];
    [_reviews release];
    [_reviewBox release];
    [super dealloc];
}

- (void) setLogo: (UIImage *)logo {    
    CGSize const MAX_LOGO_SIZE = CGSizeMake(232, 85);
    if(logo.size.height > MAX_LOGO_SIZE.height || logo.size.width > MAX_LOGO_SIZE.width) {
        CGFloat scale = MIN(MAX_LOGO_SIZE.width / logo.size.width, MAX_LOGO_SIZE.height / logo.size.height);
        logo = [UIImage imageWithCGImage:logo.CGImage scale:1.0/scale orientation:logo.imageOrientation];
    }
    self.companyLogo.image = logo;
    
    CGPoint oldCenter = self.companyLogo.center;
    self.companyLogo.frame = CGRectMake(0, 0, logo.size.width, logo.size.height);
    self.companyLogo.center = oldCenter;
}

#pragma mark - Reviews management

- (NSArray*) getReviewsLayouts: (NSArray*) reviews {
    NSMutableArray* reviewLayouts = [[[NSMutableArray alloc] initWithCapacity:reviews.count] autorelease];
    NSUInteger textReviewIndex = 0;
    for (Review* review in reviews) {
        UIView* layout;
        if (review.reviewType == ReviewTypeText) {
            layout = [[TextSampleReview alloc] initWithBackgroundColor:(textReviewIndex % 2) ? ReviewBackgroundColorYellow : ReviewBackgroundColorBlue andText:review.text];            
            textReviewIndex++;
        } else {
            layout = [[PhotoSampleReview alloc] initWithText:review.text andPhoto:((Photo*)[review.photos objectAtIndex:0]).image];
        }
        
        [reviewLayouts addObject:layout];
        [layout release];
    }
    return reviewLayouts;
}

- (void) showNextReview {
    if (self.pauseTimer) return;
    UIView* bottomMostReview = [self.reviews objectAtIndex:self.nextReviewIndex];
    [UIView animateWithDuration:TIMER_INTERVAL delay:0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
        bottomMostReview.alpha = 0;
    } completion:^(BOOL finished) {}];
    
    UIView* review = [self.reviews objectAtIndex:self.reviewIndex];
    
    
    review.alpha = 1;    
    review.transform = CGAffineTransformIdentity;
    review.center = CGPointMake(self.reviewBox.frame.size.width/2, -review.frame.size.height/2);
    [self.reviewBox addSubview:review];
    CGSize reviewSize = review.frame.size;
    CGSize boxSize = self.reviewBox.frame.size;
    
    CGFloat ty1 = reviewSize.height;
    [UIView animateWithDuration: ty1 / REVIEW_SPEED delay:0 options:UIViewAnimationOptionCurveEaseIn animations:^{        
        review.transform = CGAffineTransformMakeTranslation(0, ty1);
    } completion:^(BOOL finished) {
        CGFloat maxAlpha = (UIInterfaceOrientationIsPortrait(self.interfaceOrientation) ? MAX_ANGLE_PORTRAIT : MAX_ANGLE_LANDSCAPE);
        CGFloat alpha = (-maxAlpha + arc4random_uniform(maxAlpha*2)) * M_PI / 180.0;
        CGFloat c = sqrtf(reviewSize.width * reviewSize.width + reviewSize.height * reviewSize.height);
        CGFloat dx = c * cos(atanf(reviewSize.height / reviewSize.width ) - fabsf(alpha));
        CGFloat dy = c * cos(atanf(reviewSize.width  / reviewSize.height) - fabsf(alpha));
        
        
        CGFloat tx2 = - (boxSize.width - dx) / 2 + arc4random_uniform(boxSize.width - dx);
        
        CGFloat ddy = (dy - reviewSize.height) / 2;
        CGFloat ty2 = ddy + arc4random_uniform(boxSize.height - dy/2 - reviewSize.height / 2 - ddy);
        
        [UIView animateWithDuration: sqrtf(tx2*tx2 + ty2*ty2) / REVIEW_SPEED delay:0 options:UIViewAnimationOptionCurveEaseOut | UIViewAnimationOptionBeginFromCurrentState animations:^{
            review.transform = CGAffineTransformTranslate(review.transform,tx2, ty2);
            review.transform = CGAffineTransformRotate(review.transform, alpha);
        } completion:^(BOOL finished) {}];
    }]; 
        
    self.reviewIndex = self.nextReviewIndex;
}

- (NSUInteger) nextReviewIndex {
    return (self.reviewIndex == self.reviews.count -1) ? 0 : self.reviewIndex + 1;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    self.pauseTimer = YES;
    for (UIView* view in self.reviewBox.subviews) {
        [view removeFromSuperview];
    }
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    self.pauseTimer = NO;
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation {
    UIColor* background;    
    if (UIInterfaceOrientationIsPortrait(orientation)) { //PORTRAIT
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"splash_bg_portrait.png"]];        
        self.startBtn.frame = CGRectMake(305, 295, 158, 158);
        self.socialButtonsView.center = CGPointMake(384, 230.5);
        self.shareYourSmileImg.frame = CGRectMake(128, 125, 507, 51);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_portrait.png"];
        self.companyLogo.frame = CGRectMake(268, 17, 232, 85);
        self.reviewBox.frame = CGRectMake(15, 500, 740, 500);
    } else { //LANDSCAPE
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"splash_bg_landscape.png"]];
        self.startBtn.frame = CGRectMake(701, 444, 272, 272);
        self.socialButtonsView.center = CGPointMake(837, 342);
        self.shareYourSmileImg.frame = CGRectMake(682, 180, 310, 98);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_landscape.png"];
        self.companyLogo.frame = CGRectMake(721, 47, 232, 85);
        self.reviewBox.frame = CGRectMake(15, 20, 620, 720);
    }
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)showWelcomeScreen:(id)sender {
    [RootController switchToController:@"WelcomeScreenController" rootController:self.rootController];
}
@end
