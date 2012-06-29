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
@property (assign, nonatomic) UIView* currentReview;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (NSArray*) getReviewsLayouts: (NSArray*) reviews;
- (void) showNextReview;
- (NSUInteger) nextReviewIndex;
- (void) setLogo: (UIImage *)logo;
@end;

@implementation SplashScreenController

static CGFloat const TIMER_INTERVAL = 3;
static CGFloat const REVIEW_SPEED = 400; //pixel / second
static CGFloat const MAX_ANGLE_PORTRAIT = 12;
static CGFloat const MAX_ANGLE_LANDSCAPE = 40;
static CGFloat const REVIEW_SCALE = 0.7;

@synthesize startBtn = _startBtn;
@synthesize socialButtonsView = _socialButtonsView;
@synthesize shareYourSmileImg = _shareYourSmileImg;
@synthesize companyLogo = _companyLogo;
@synthesize reviewBox = _reviewBox;
@synthesize poweredByImg = _poweredByImg;
@synthesize buttonBgView = _buttonBgView;
@synthesize logoBgView = _logoBgView;
@synthesize dao = _dao;
@synthesize reviews = _reviews;
@synthesize timer = _timer;
@synthesize reviewIndex = _reviewIndex;
@synthesize pauseTimer = _pauseTimer;
@synthesize currentReview = _currentReview;

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
    self.socialButtonsView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"socials_bg.png"]];
    self.logoBgView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"welcome_logo_bg.png"]];
    [self layoutSubviewsForInterfaceOrientation:self.interfaceOrientation];
}

- (void)viewDidAppear:(BOOL)animated {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:TIMER_INTERVAL target:self selector:@selector(showNextReview) userInfo:nil repeats:YES];
    [self showNextReview];
}

- (void)viewDidDisappear:(BOOL)animated {
    [self.timer invalidate];
    self.timer = nil;
}

- (void)viewDidUnload
{
    [self setStartBtn:nil];
    [self setSocialButtonsView:nil];
    [self setShareYourSmileImg:nil];
    [self setCompanyLogo:nil];
    [self setReviewBox:nil];
    [self setPoweredByImg:nil];
    [self setButtonBgView:nil];
    [self setLogoBgView:nil];
    [super viewDidUnload];
}

- (void)dealloc {
    self.rootController = nil;
    self.dao = nil;
    self.reviews = nil;
    [_startBtn release];
    [_socialButtonsView release];
    [_shareYourSmileImg release];
    [_companyLogo release];
    [_reviewBox release];
    [_poweredByImg release];
    [_buttonBgView release];
    [_logoBgView release];
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
            id photo = (Photo*)[review.photos objectAtIndex:0];
            layout = [[PhotoSampleReview alloc] initWithText:review.text andPhoto: (photo && photo!=[NSNull null]) ? [photo image] : nil];
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
    
    self.currentReview = [self.reviews objectAtIndex:self.reviewIndex];
    self.currentReview.alpha = 1;    
    self.currentReview.transform = CGAffineTransformIdentity;
    self.currentReview.center = CGPointMake(self.reviewBox.frame.size.width/2, -self.currentReview.frame.size.height/2);
    self.currentReview.layer.shadowColor = [UIColor blackColor].CGColor;
    self.currentReview.layer.shadowRadius = 30;
    self.currentReview.layer.shadowOffset = CGSizeMake(0, 30);
    [self.reviewBox addSubview:self.currentReview];
    
    CGSize reviewCurrentSize = self.currentReview.frame.size;
    CGSize reviewFinalSize = CGSizeMake(self.currentReview.frame.size.width * REVIEW_SCALE, self.currentReview.frame.size.height * REVIEW_SCALE);
    CGSize boxSize = self.reviewBox.frame.size;
    
    //1 - polaroid
    CGFloat ty1 = reviewCurrentSize.height;
    [UIView animateWithDuration: ty1 / REVIEW_SPEED delay:0 options:UIViewAnimationOptionCurveEaseIn animations:^{        
        self.currentReview.transform = CGAffineTransformMakeTranslation(0, ty1);
    } completion:^(BOOL finished) {
        //2 - falling
        CGFloat maxAlpha = (UIInterfaceOrientationIsPortrait(self.interfaceOrientation) ? MAX_ANGLE_PORTRAIT : MAX_ANGLE_LANDSCAPE);
        CGFloat alpha = (-maxAlpha + arc4random_uniform(maxAlpha*2)) * M_PI / 180.0;
        CGFloat diagonal = sqrtf(reviewFinalSize.width * reviewFinalSize.width + reviewFinalSize.height * reviewFinalSize.height);
        CGSize rotatedReviewBoxSize = CGSizeMake(diagonal * cos(atanf(reviewFinalSize.height / reviewFinalSize.width ) - fabsf(alpha)), 
                                                 diagonal * cos(atanf(reviewFinalSize.width  / reviewFinalSize.height) - fabsf(alpha)));
        
        
        CGFloat ddy = (rotatedReviewBoxSize.height - reviewCurrentSize.height) / 2;
        
        CGSize fallingOffset = CGSizeMake( -(boxSize.width - rotatedReviewBoxSize.width) / 2 + arc4random_uniform(boxSize.width - rotatedReviewBoxSize.width),
                                           ddy + arc4random_uniform(boxSize.height - rotatedReviewBoxSize.height/2 - reviewCurrentSize.height / 2 - ddy));
        CGFloat fallingDistance = sqrtf(powf(fallingOffset.width, 2) + powf(fallingOffset.height, 2));
        CGFloat fallingDuration = fallingDistance / REVIEW_SPEED;
        
        [UIView animateWithDuration: fallingDuration delay:0 options:UIViewAnimationOptionCurveEaseOut | UIViewAnimationOptionBeginFromCurrentState animations:^{
            self.currentReview.transform = CGAffineTransformTranslate(self.currentReview.transform,fallingOffset.width, fallingOffset.height);
            self.currentReview.transform = CGAffineTransformRotate(self.currentReview.transform, alpha);
            self.currentReview.transform = CGAffineTransformScale(self.currentReview.transform, REVIEW_SCALE, REVIEW_SCALE);
            
            CABasicAnimation* shadowOffset = [CABasicAnimation animationWithKeyPath:@"shadowOffset"];
            shadowOffset.delegate = self;
            shadowOffset.duration = fallingDuration;
            shadowOffset.fromValue = [NSValue valueWithCGSize:self.currentReview.layer.shadowOffset];
            shadowOffset.toValue = [NSValue valueWithCGSize:CGSizeMake(0, 0)];
            [self.currentReview.layer addAnimation:shadowOffset forKey:@"shadowOffset"];
            
            CABasicAnimation* shadowRadius = [CABasicAnimation animationWithKeyPath:@"shadowRadius"];
            shadowRadius.delegate = self;
            shadowRadius.duration = fallingDuration;
            shadowRadius.fromValue = [NSNumber numberWithFloat:self.currentReview.layer.shadowRadius];
            shadowRadius.toValue = [NSNumber numberWithFloat:0.0];
            [self.currentReview.layer addAnimation:shadowRadius forKey:@"shadowRadius"];            
        } completion:^(BOOL finished) {}];
    }]; 
        
    self.reviewIndex = self.nextReviewIndex;
}

- (void)animationDidStop:(CAAnimation *)anim finished:(BOOL)flag {
    self.currentReview.layer.shadowRadius = 0;
    self.currentReview.layer.shadowColor = [UIColor clearColor].CGColor;
}

- (NSUInteger) nextReviewIndex {
    return (self.reviewIndex == self.reviews.count -1) ? 0 : self.reviewIndex + 1;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
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
    if (UIInterfaceOrientationIsPortrait(orientation)) { //PORTRAIT
        self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"splash_bg_portrait.png"]];
        self.buttonBgView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"startbtn_bg_portrait.png"]];
        self.buttonBgView.frame = CGRectMake(298, 287, 172, 173);
        self.startBtn.frame = CGRectMake(7, 7.5, 158, 158);
        self.socialButtonsView.center = CGPointMake(384, 230.5);
        self.shareYourSmileImg.frame = CGRectMake(128, 125, 507, 51);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_portrait.png"];
        self.logoBgView.center = CGPointMake(384, 61);
        self.reviewBox.frame = CGRectMake(30, 500, 709, 484);
        self.poweredByImg.frame = CGRectMake(555, 448, 203, 30);
    } else { //LANDSCAPE
        self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"splash_bg_landscape.png"]];
        self.buttonBgView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"startbtn_bg_landscape.png"]];
        self.startBtn.frame = CGRectMake(4, 5, 272, 272);
        self.buttonBgView.frame = CGRectMake(697, 439, 280, 281);
        self.socialButtonsView.center = CGPointMake(837, 342);
        self.shareYourSmileImg.frame = CGRectMake(682, 180, 310, 98);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_landscape.png"];
        self.logoBgView.center = CGPointMake(837, 90);
        self.reviewBox.frame = CGRectMake(30, 20, 592, 708);
        self.poweredByImg.frame = CGRectMake(811, 728, 203, 30);
    }
}

#pragma mark - Actions

- (IBAction)showWelcomeScreen:(id)sender {
    [RootController switchToController:@"WelcomeScreenController" rootController:self.rootController];
}
@end
