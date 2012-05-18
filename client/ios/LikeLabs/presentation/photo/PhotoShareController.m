#import "PhotoShareController.h"
#import "RootPhotoController.h"
#import <AVFoundation/AVFoundation.h>
#import "Review.h"
#import "PhotoOverlayController.h"
#import "User.h"
#import "PhotoRecipientsOverlayController.h"

@interface PhotoShareController ()
@property (nonatomic, retain) RootPhotoController* rootController;
@property (nonatomic, assign) Review* review;
@property (nonatomic, retain) PhotoOverlayController* overlay;
@property (nonatomic, retain) PhotoRecipientsOverlayController* recipientsOverlay;
@end

@implementation PhotoShareController
@synthesize imageView = _imageView;
@synthesize phoneField = _phoneField;
@synthesize addRecipientsView = _addRecipientsView;
@synthesize submitBtn = _submitBtn;
@synthesize mailButton = _mailButton;
@synthesize phoneButton = _phoneButton;
@synthesize rootController = _rootController;
@synthesize review = _review;
@synthesize overlay = _overlay;
@synthesize recipientsOverlay = _recipientsOverlay;

#pragma mark - Initialization

-(id)initWithRootController:(RootPhotoController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.review = rootController.rootController.review;
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
    
    _imageView = [[UIImageWithReview alloc] initWithFrame:CGRectMake(24, 191, 468, 350) image:[self.review.photos objectAtIndex:self.review.reviewPhotoIndex] andText:self.review.text];    
    [self.view addSubview:self.imageView];

    self.mailButton.enabled = self.phoneButton.enabled = (self.review.contacts.count < MAX_CONTACTS);
    
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setImageView:nil];
    [self setPhoneField:nil];
    [self setAddRecipientsView:nil];
    [self setSubmitBtn:nil];
    [self setMailButton:nil];
    [self setPhoneButton:nil];
    [super viewDidUnload];
    self.rootController = nil;
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_phoneField release];
    [_addRecipientsView release];
    [_submitBtn release];
    [_mailButton release];
    [_phoneButton release];
    [super dealloc];
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
}

- (void) setBackgroundForInterfaceOrientation:(UIInterfaceOrientation) orientation {
    UIColor *background = [[UIColor alloc] initWithPatternImage: [UIImage imageNamed:UIInterfaceOrientationIsLandscape(orientation) ? @"photo_share_bg_landscape.png" : @"photo_share_bg_portrait.png"]];
    self.view.backgroundColor = background;
    [background release];
}

- (void) layoutSubviewsForInterfaceOrientation: (UIInterfaceOrientation) orientation {
    [self setBackgroundForInterfaceOrientation:orientation];
    if (UIInterfaceOrientationIsLandscape(orientation)) {
        //LANDSCAPE
        self.imageView.frame = CGRectMake(24, 191, 468, 350);
        self.phoneField.frame = CGRectMake(581, 402, 389, 51);
        self.addRecipientsView.frame = CGRectMake(832, 498, 138, 53);
        self.submitBtn.frame = CGRectMake(548, 568, 422, 86);
        [self.submitBtn setBackgroundImage:[UIImage imageNamed:@"submit_btn_bg_landscape.png"] forState:UIControlStateNormal];        
    } else {
        //PRTRAIT
        self.imageView.frame = CGRectMake(142, 180, 468, 350);
        self.phoneField.frame = CGRectMake(108, 789, 442, 51);
        self.addRecipientsView.frame = CGRectMake(561, 789, 142, 53);
        self.submitBtn.frame = CGRectMake(47, 894, 665, 86);
        [self.submitBtn setBackgroundImage:[UIImage imageNamed:@"submit_btn_bg.png"] forState:UIControlStateNormal];        
    }
    [self.imageView setPhoto:[self.review.photos objectAtIndex:self.review.reviewPhotoIndex]];
}

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    return NO;
}

#pragma mark - Actions
- (IBAction)fieldTouched:(id)sender {
    _overlay = [[PhotoOverlayController alloc] initWithPhone:self.review.user.phoneNumber];
    [self.rootController.view addSubview:self.overlay.view];
}

- (IBAction)addMail:(id)sender {
    _recipientsOverlay = [[PhotoRecipientsOverlayController alloc] initWithContactType:ContactTypeEmail andRecipients:self.review.contacts];
    [self.rootController.view addSubview:self.recipientsOverlay.view];
}

- (IBAction)addPhone:(id)sender {
    _recipientsOverlay = [[PhotoRecipientsOverlayController alloc] initWithContactType:ContactTypePhone andRecipients:self.review.contacts];
    [self.rootController.view addSubview:self.recipientsOverlay.view];
}

- (IBAction)submit:(id)sender {
    [self.rootController step];
}

- (void)dismissOverlay {
    [self.overlay.view removeFromSuperview];
    self.overlay = nil;
    [_overlay release];
}

- (void)savePhone {
    self.phoneField.text = self.review.user.phoneNumber = self.overlay.phone;
    [self dismissOverlay];
    [self layoutSubviewsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

- (void)dismissRecipientsOverlay {
    [self.recipientsOverlay.view removeFromSuperview];
    self.recipientsOverlay = nil;
    [_recipientsOverlay release];
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
