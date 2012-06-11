#import "PhotoFinishedController.h"
#import "RootPhotoController.h"
#import "UIImageWithReview.h"
#import <AVFoundation/AVFoundation.h>

@interface PhotoFinishedController ()
@property (nonatomic, retain) UIViewController <ContainerController>* rootController;
@property (nonatomic, assign) Review* review;
@property (nonatomic, retain) UIImageWithReview* imageView;
@property (retain, nonatomic) UIImageView *messageView;
- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation)orientation;
@end

@implementation PhotoFinishedController
@synthesize button = _button;
@synthesize lbMessageSent = _lbMessageSent;
@synthesize thankYouImg = _thankYouImg;
@synthesize instructionsBackground = _instructionsBackground;
@synthesize rootController = _rootController;
@synthesize imageView = _imageView;
@synthesize review = _review;
@synthesize messageView = _messageView;

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
    self.view.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;    
    self.lbMessageSent.text = (self.review.contacts.count) ? [NSString stringWithFormat:@"%d messages were sent successfully!", self.review.contacts.count + 1] : @"Message was sent successfully";
    if(self.review.reviewType==ReviewTypePhoto){
        _imageView = [[UIImageWithReview alloc] initWithFrame:CGRectMake(24, 211, 468, 350) image:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image andText:self.review.text];    
        [self.view addSubview:self.imageView];
    } else {
        self.messageView = [[[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"promo_bg_gray.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:5]] autorelease];
        [self.messageView setFrame:CGRectMake(24, 211, 468, 350)];
        UILabel *lbText = [[[UILabel alloc] initWithFrame:CGRectMake(10, 10, self.messageView.frame.size.width-20, self.messageView.frame.size.height-20)] autorelease];
        self.messageView.opaque = NO;
        self.messageView.layer.shadowColor = [UIColor blackColor].CGColor;
        self.messageView.layer.shadowOffset = CGSizeMake(5, 5);
        self.messageView.layer.shadowOpacity = 0.8;
        self.messageView.layer.shadowRadius = 10;
        
        lbText.contentMode = UIViewContentModeScaleToFill;
        lbText.font = [UIFont fontWithName:@"BadScript-Regular" size:20];
        [lbText setBackgroundColor:[UIColor clearColor]];
        [lbText setNumberOfLines:0];
        [lbText setText:self.review.text];
        [lbText setTextAlignment:UITextAlignmentCenter];
        
        [self.messageView addSubview:lbText];
        [self.view addSubview:self.messageView];
    }
    [self.lbMessageSent sizeToFit];

    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setButton:nil];
    [self setLbMessageSent:nil];
    [self setThankYouImg:nil];
    [self setInstructionsBackground:nil];
    [super viewDidUnload];
    [self setMessageView:nil];
    self.rootController = nil;
    self.imageView = nil;
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_button release];
    [_messageView release];
    [_lbMessageSent release];
    [_thankYouImg release];
    [_instructionsBackground release];
    [super dealloc];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation)orientation {
    [self setBackgroundForInterfaceOrientation:orientation];
    CGRect contentFrame;
    if (UIInterfaceOrientationIsLandscape(orientation)) {   //LANDSCAPE
        [self.button setBackgroundImage:[UIImage imageNamed:@"return_to_home_screen_btn_landscape.png"] forState:UIControlStateNormal];
        self.thankYouImg.center = CGPointMake(512, self.thankYouImg.center.y);
        self.instructionsBackground.frame = CGRectMake(530, 211, 455, 282);
        self.instructionsBackground.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"thankyou_instructions_bg_landscape.png"]];
        contentFrame = CGRectMake(24, 211, 468, 350);
    } else {                                                //PORTRAIT
        [self.button setBackgroundImage:[UIImage imageNamed:@"return_to_home_screen_btn_portrait.png"] forState:UIControlStateNormal];
        self.thankYouImg.center = CGPointMake(384, self.thankYouImg.center.y);
        self.instructionsBackground.frame = CGRectMake(37, 600, 697, 287);
        self.instructionsBackground.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"thankyou_instructions_bg_portrait.png"]];
        contentFrame = CGRectMake(142, 210, 468, 350);
    }
    self.lbMessageSent.center = CGPointMake(self.instructionsBackground.frame.size.width / 2, self.lbMessageSent.center.y);
    if (self.review.reviewType == ReviewTypePhoto) {
        self.imageView.frame = contentFrame;
        [self.imageView setPhoto:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image];
    } else {
        self.messageView.frame = contentFrame;
    }    
}

- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation {
    self.view.backgroundColor = [UIColor colorWithPatternImage: [UIImage imageNamed:UIInterfaceOrientationIsLandscape(orientation) ? @"welcome_bg_landscape.png" : @"welcome_bg_portrait.png"]];
}

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [(RootPhotoController*)self.rootController goHome:sender];
}
@end
