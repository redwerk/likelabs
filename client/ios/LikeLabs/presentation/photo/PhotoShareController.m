#import "PhotoShareController.h"
#import "RootPhotoController.h"
#import <AVFoundation/AVFoundation.h>
#import "Review.h"
#import "PhotoOverlayController.h"
#import "User.h"
#import "PhotoRecipientsOverlayController.h"
#import "ReviewService.h"

static NSString *const RECIPITENTS_COUNT_LABEL_TEMPLATE = @"Send to additional recipients (%d of %d)";

@interface PhotoShareController ()
@property (nonatomic, retain) UIViewController <ContainerController>* rootController;
@property (nonatomic, assign) Review* review;
@property (nonatomic, assign) ReviewService* reviewService;
@property (nonatomic, retain) PhotoOverlayController* overlay;
@property (nonatomic, retain) PhotoRecipientsOverlayController* recipientsOverlay;
- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation;
- (void) setRecipientsLabelText;
@end

@implementation PhotoShareController
@synthesize imageView = _imageView;
@synthesize messageView = _messageView;
@synthesize phoneField = _phoneField;
@synthesize addRecipientsView = _addRecipientsView;
@synthesize submitBtn = _submitBtn;
@synthesize mailButton = _mailButton;
@synthesize phoneButton = _phoneButton;
@synthesize lbTitle = _lbTitle;
@synthesize instructionsBackground = _instructionsBackground;
@synthesize thirdStepLabel = _thirdStepLabel;
@synthesize requiredLabel = _requiredLabel;
@synthesize recipientsCountLabel = _recipientsCountLabel;
@synthesize rootController = _rootController;
@synthesize review = _review;
@synthesize overlay = _overlay;
@synthesize recipientsOverlay = _recipientsOverlay;
@synthesize reviewService = _reviewService;

#pragma mark - Initialization

-(id)initWithRootController:(UIViewController <ContainerController> *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.review = [self.rootController getReview];
        self.reviewService = [rootController getReviewService];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dismissOverlay) name:kPrimaryPhoneDidCancel object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(savePhone) name:kPrimaryPhoneDone object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dismissRecipientsOverlay) name:kRecipientsDidCancel object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(saveRecipient) name:kRecipientsDone object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;    
    NSString *titleFormat = @"Share your %@ with your friends!";
    [self.lbTitle setFont:[UIFont fontWithName:@"Lobster 1.4" size:45]];
    [self.lbTitle setFrame:CGRectMake(0, 0, 700, 50)];

    if(self.review.reviewType == ReviewTypeText){
        self.lbTitle.text = [[[NSString alloc] initWithFormat:titleFormat, @"message"] autorelease];
        self.messageView = [[[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"promo_bg_gray.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:5]] autorelease];
        [self.messageView setFrame:CGRectMake(24, 191, 468, 350) ];
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
    } else {
        self.lbTitle.text = [[[NSString alloc] initWithFormat:titleFormat, @"photo message"] autorelease];
        _imageView = [[UIImageWithReview alloc] initWithFrame:CGRectMake(24, 191, 468, 350) image:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image andText:self.review.text];    
        [self.view addSubview:self.imageView];
    }
    
    [self setRecipientsLabelText];
    self.mailButton.enabled = self.phoneButton.enabled = (self.review.contacts.count < MAX_CONTACTS);
    
    if (self.review.user.phone && self.review.user.phone.length) {
        self.phoneField.text = self.review.user.phone;
    }
    
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setImageView:nil];
    [self setMessageView:nil];
    [self setPhoneField:nil];
    [self setAddRecipientsView:nil];
    [self setSubmitBtn:nil];
    [self setMailButton:nil];
    [self setPhoneButton:nil];
    [self setLbTitle:nil];
    [self setInstructionsBackground:nil];
    [self setThirdStepLabel:nil];
    [self setRequiredLabel:nil];
    [self setRecipientsCountLabel:nil];
    [self setRootController:nil];
    [self setOverlay:nil];
    [self setRecipientsOverlay:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [super viewDidUnload];
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_messageView release];
    [_phoneField release];
    [_addRecipientsView release];
    [_submitBtn release];
    [_mailButton release];
    [_phoneButton release];
    [_lbTitle release];
    [_instructionsBackground release];
    [_thirdStepLabel release];
    [_requiredLabel release];
    [_recipientsCountLabel release];
    [_rootController release];
    [_overlay release];
    [_recipientsOverlay release];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [super dealloc];
}

- (void)viewDidDisappear:(BOOL)animated {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [super viewDidDisappear:animated];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self layoutSubviewsForInterfaceOrientation:toInterfaceOrientation];
    [self.overlay willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    [self.recipientsOverlay willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
}

- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation {
    UIColor *background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:UIInterfaceOrientationIsLandscape(orientation) ? @"welcome_bg_landscape.png" : @"welcome_bg_portrait.png"]];
    self.view.backgroundColor = background;
    [background release];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation {
    [self setBackgroundForInterfaceOrientation:orientation];
    if(self.review.reviewType==ReviewTypeText){        
        if (UIInterfaceOrientationIsLandscape(orientation)) {            
            //self.messageView.image = [UIImage imageNamed:@"text-block-portrait.png"];
            self.messageView.frame = CGRectMake(24, 191, 468, 350);
        } else {
            //self.messageView.image = [UIImage imageNamed:@"text-block-landscape.png"];
            self.messageView.frame = CGRectMake(142, 180, 468, 350);   
        }
    } else {
        if (UIInterfaceOrientationIsLandscape(orientation)) {
            //LANDSCAPE
            self.imageView.frame = CGRectMake(24, 191, 468, 350);
        } else {
            //PRTRAIT
            self.imageView.frame = CGRectMake(142, 180, 468, 350);        
        }
        [self.imageView setPhoto:((Photo*)[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]).image];
    }
    
    if (UIInterfaceOrientationIsLandscape(orientation)) {
        [self.lbTitle setCenter:CGPointMake(1024/2, 120)];
        self.phoneField.frame = CGRectMake(64, 226, 375, 51);
        self.addRecipientsView.frame = CGRectMake(300, 312, 138, 53);
        self.thirdStepLabel.frame = CGRectMake(69, 118, 361, 74);
        [self.submitBtn setBackgroundImage:[UIImage imageNamed:@"submit_btn_bg_landscape.png"] forState:UIControlStateNormal];
        self.instructionsBackground.frame = CGRectMake(531, 195, 455, 482);
        self.instructionsBackground.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"share_instructions_bg_landscape.png"]];
        self.requiredLabel.frame = CGRectMake(95, 280, 340, 21);
        self.recipientsCountLabel.frame = CGRectMake(16, 327, 280, 21);
    } else {
        [self.lbTitle setCenter:CGPointMake(768/2, 120)];
        self.phoneField.frame = CGRectMake(64, 226, 442, 51);
        self.addRecipientsView.frame = CGRectMake(525, 226, 142, 53);
        self.thirdStepLabel.frame = CGRectMake(69, 118, 600, 54);
        [self.submitBtn setBackgroundImage:[UIImage imageNamed:@"submit_btn_bg.png"] forState:UIControlStateNormal];  
        self.instructionsBackground.frame = CGRectMake(32, 555, 697, 438);        
        self.instructionsBackground.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"share_instructions_bg_portrait.png"]];
        self.requiredLabel.frame = CGRectMake(67, 280, 340, 21);
        self.recipientsCountLabel.frame = CGRectMake(387, 200, 280, 21);
    }
}

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    return NO;
}

#pragma mark - Actions
- (IBAction)fieldTouched:(id)sender {
    _overlay = [[PhotoOverlayController alloc] initWithPhone:self.review.user.phone];
    [self.rootController.view addSubview:self.overlay.view];
}

- (IBAction)addMail:(id)sender {
    self.recipientsOverlay = [[[PhotoRecipientsOverlayController alloc] initWithContactType:ContactTypeEmail andRecipients:self.review.contacts] autorelease];
    [self.rootController.view addSubview:self.recipientsOverlay.view];
}

- (IBAction)addPhone:(id)sender {
    self.recipientsOverlay = [[[PhotoRecipientsOverlayController alloc] initWithContactType:ContactTypePhone andRecipients:self.review.contacts] autorelease];
    [self.rootController.view addSubview:self.recipientsOverlay.view];
}

- (IBAction)submit:(id)sender {
    [self.reviewService postReview:self.review];
    [self.rootController step];
}

- (void)dismissOverlay {
    [self.overlay.view removeFromSuperview];
    self.overlay = nil;
}

- (void)savePhone {
    self.phoneField.text = self.review.user.phone = self.overlay.phone;
    [self dismissOverlay];
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

- (void)dismissRecipientsOverlay {
    [self setRecipientsLabelText];
    [self.recipientsOverlay.view removeFromSuperview];
    self.recipientsOverlay = nil;
}

- (void) setRecipientsLabelText {
    self.recipientsCountLabel.text = (self.review.contacts.count) ? [NSString stringWithFormat:RECIPITENTS_COUNT_LABEL_TEMPLATE, self.review.contacts.count, MAX_CONTACTS] : @"Send to additional recipients (5 max)";    
}

- (void)saveRecipient {
    self.review.contacts = self.recipientsOverlay.recipients;
    if (self.review.contacts.count == MAX_CONTACTS) {
        self.mailButton.enabled = self.phoneButton.enabled = NO;
    }
    [self dismissRecipientsOverlay];
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

@end
