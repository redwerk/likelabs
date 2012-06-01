#import "SplashScreenController.h"
#import "RootController.h"
#import "SettingsDao.h"
#import "Review.h"
#import "TextSampleReview.h"
#import "PhotoSampleReview.h"

@interface SplashScreenController()
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) SettingsDao* dao;
@property (retain, nonatomic) NSArray* reviews;
@property (retain, nonatomic) NSTimer* timer;
@property (assign, nonatomic) NSUInteger reviewIndex;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (NSArray*) getReviewsLayouts: (NSArray*) reviews;
- (void) showNextReview;
- (void) setLogo: (UIImage *)logo;
@end;

@implementation SplashScreenController

static CGFloat const TIMER_INTERVAL = 1;

@synthesize startBtn = _startBtn;
@synthesize socialButtonsView = _socialButtonsView;
@synthesize shareYourSmileImg = _shareYourSmileImg;
@synthesize companyLogo = _companyLogo;
@synthesize reviewBox = _reviewBox;
@synthesize dao = _dao;
@synthesize reviews = _reviews;
@synthesize timer = _timer;
@synthesize reviewIndex = _reviewIndex;

@synthesize rootController = _rootController;

#pragma mark - View lifecycle

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        _dao = [[SettingsDao alloc] init];
        self.reviews = [self getReviewsLayouts: self.dao.promoReviews];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self setLogo:self.dao.logo];
    [self layoutSubviewsForInterfaceOrientation:self.interfaceOrientation];
}

- (void)viewDidAppear:(BOOL)animated {
    _timer = [NSTimer scheduledTimerWithTimeInterval:TIMER_INTERVAL target:self selector:@selector(showNextReview) userInfo:nil repeats:YES];
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
    UIView* review = [self.reviews objectAtIndex:self.reviewIndex];
    review.center = CGPointMake(self.reviewBox.frame.size.width/2, self.reviewBox.frame.size.height/2);
    [self.reviewBox addSubview:review];
    if (self.reviewIndex == self.reviews.count - 1) {
        self.reviewIndex = 0;
        [[self.reviewBox.subviews objectAtIndex:0] removeFromSuperview];
    } else {
        self.reviewIndex++;
    }
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
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
        self.reviewBox.frame = CGRectMake(21, 511, 734, 495);
    } else { //LANDSCAPE
        background = [[UIColor alloc] initWithPatternImage:[UIImage imageNamed:@"splash_bg_landscape.png"]];
        self.startBtn.frame = CGRectMake(701, 444, 272, 272);
        self.socialButtonsView.center = CGPointMake(837, 342);
        self.shareYourSmileImg.frame = CGRectMake(682, 180, 310, 98);
        self.shareYourSmileImg.image = [UIImage imageNamed:@"share_your_smile_landscape.png"];
        self.companyLogo.frame = CGRectMake(721, 47, 232, 85);
        self.reviewBox.frame = CGRectMake(24, 32, 610, 710);
    }
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)showWelcomeScreen:(id)sender {
    [RootController switchToController:@"WelcomeScreenController" rootController:self.rootController];
}
@end
