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
    
    if(self.review.reviewType==ReviewTypePhoto){
        self.lbMessageSent.text = @"5 messages were sent successfully!";
        _imageView = [[UIImageWithReview alloc] initWithFrame:CGRectMake(24, 211, 468, 350) image:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image andText:self.review.text];    
        [self.view addSubview:self.imageView];
    } else {
        self.lbMessageSent.text = @"Message were sent successfully!";
        self.messageView = [[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"text-block-landscape.png"]] autorelease];
        [self.messageView setFrame:CGRectMake(24, 191, 468, 350)];
        UILabel *lbText = [[[UILabel alloc] initWithFrame:CGRectMake(10, 10, self.messageView.frame.size.width-20, self.messageView.frame.size.height-20)] autorelease];
        self.messageView.opaque = NO;
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
        self.button.frame = CGRectMake(552, 387, 422,86);
        self.lbMessageSent.center = CGPointMake(760, 255);
    } else {
        //PORTRAIT
        self.button.frame = CGRectMake(47, 895, 667,86);
        self.lbMessageSent.center = CGPointMake(768/2, 760);
    }
    if(self.review.reviewType==ReviewTypePhoto){
        if (UIInterfaceOrientationIsLandscape(orientation)) {
            //LANDSCAPE
            self.imageView.frame = CGRectMake(24, 211, 468, 350);
        } else {
        //PORTRAIT
            self.imageView.frame = CGRectMake(142, 275, 468, 350);
        }
        [self.imageView setPhoto:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image];
    } else {
        if (UIInterfaceOrientationIsLandscape(orientation)) {            
            self.messageView.frame = CGRectMake(24, 191, 468, 350);
        } else {
            self.messageView.frame = CGRectMake(142, 230, 468, 350);   
        }
    }

}

- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation {
    UIColor *background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:UIInterfaceOrientationIsLandscape(orientation) ? @"thankyou_bg_landscape.png" : @"thankyou_bg_portrait.png"]];
    self.view.backgroundColor = background;
    [background release];
}

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [(RootPhotoController*)self.rootController goHome:sender];
}
@end
