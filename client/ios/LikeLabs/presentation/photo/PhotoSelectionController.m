#import <AVFoundation/AVFoundation.h>
#import "PhotoSelectionController.h"
#import "RootController.h"
#import "RootPhotoController.h"
#import "Review.h"

@interface PhotoSelectionController ()
@property (nonatomic, retain) UIViewController <ContainerController> *rootController;
@property (nonatomic, assign) Review *review;
@property (nonatomic, assign) UIButton *btnDeleteThumbnail;
- (void) populateWithPhotos;
- (void) setPhoto: (UIImage *)photo;
- (void) resetThumbnails;
- (UIImage*)imageWithBorderFromImage:(UIImage*)source;
- (void) deletePhoto: (UIButton *) sender;
- (void) selectThumbnail:(UIView*) thumbnail;
- (void) dimThumbnail: (UIView*) thumbnail;
- (BOOL) selectNewPhotoBeforeDeletingPhotoAtIndex:(NSUInteger) deletedPhotoIndex;
- (UIImageView *) thumbnailAtIndex:(NSInteger) index;
- (void) moveDeleteBotton;
@end

@implementation PhotoSelectionController
static NSString *const bgLandscape = @"photo_selection_bg_landscape.png";
static NSString *const bgPortrait = @"photo_selection_bg_portrait.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
const float selectedScaleFactor = 1.5;
const float deletedPhotoAlpha = 0.5;
const int thumbnailsTagOffset = 55;


@synthesize imageView = _imageView;
@synthesize thumbnailsView = _thumbnailsView;
@synthesize submitButton = _submitButton;
@synthesize rootController = _rootController;
@synthesize review = _review;
@synthesize btnDeleteThumbnail = _btnDeleteThumbnail;

#pragma mark - Initialization
- (id)initWithRootController:(UIViewController <ContainerController> *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.review = [self.rootController getReview];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    [self willAnimateRotationToInterfaceOrientation:[self interfaceOrientation] duration:0]; 
   // [self didRotateFromInterfaceOrientation:self.interfaceOrientation];
    [self selectThumbnail:[self.thumbnailsView.subviews objectAtIndex:self.review.reviewPhotoIndex]];
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOpacity = 0.8;
    self.imageView.layer.shadowOffset = CGSizeMake(0, 5);
    self.imageView.layer.shadowRadius = 10;
    self.imageView.layer.shadowPath = [UIBezierPath bezierPathWithRect:self.imageView.bounds].CGPath;
    self.btnDeleteThumbnail = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage *btnImg = [UIImage imageNamed:@"delete_btn.png"];
    [self.btnDeleteThumbnail setImage:btnImg forState:UIControlStateNormal];
    
    self.btnDeleteThumbnail.frame = CGRectMake(0, 0, btnImg.size.width, btnImg.size.height);
    [self.btnDeleteThumbnail addTarget:self action:@selector(deletePhoto:) forControlEvents:UIControlEventTouchDown];
    [self.thumbnailsView addSubview:self.btnDeleteThumbnail];
}

#pragma mark - Memory management;

- (void)viewDidUnload
{
    [self setRootController:nil];
    [self setImageView:nil];
    [self setThumbnailsView:nil];

    [self setSubmitButton:nil];
    [self setBtnDeleteThumbnail:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_thumbnailsView release];
    [_submitButton release];
    [_btnDeleteThumbnail release];
    [super dealloc];
}

#pragma mark - Photo management

- (void)populateWithPhotos {
    CGPoint photosOffset = CGPointMake(0, 35);
    CGSize maxPhotoSize = CGSizeMake(128.0, 114.0);
    int photosPadding = 10;
    for (int i=0; i<self.review.photos.count; i++) {
        UIImageView *imageView = [self thumbnailAtIndex:(i+thumbnailsTagOffset)];
        if(!imageView) {
            Photo* reviewPhoto = [self.review.photos objectAtIndex:i];
            UIImage *photo = reviewPhoto.image;
            CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
            
            photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
                              imageView = [[UIImageView alloc] initWithImage:[self imageWithBorderFromImage:photo]];
            
            [self.thumbnailsView addSubview:imageView];
            
            imageView.layer.shadowColor = [UIColor blackColor].CGColor;
            imageView.layer.shadowOffset = CGSizeMake(0, 6);
            imageView.layer.shadowOpacity = 0.75;
            imageView.layer.shadowRadius = 11;
            imageView.layer.shadowPath = [UIBezierPath bezierPathWithRect:imageView.bounds].CGPath;
            imageView.tag = i + thumbnailsTagOffset;
            imageView.userInteractionEnabled = YES;
            if (reviewPhoto.status == PhotoStatusDeleted) {
                [self dimThumbnail:imageView];
            } else {
                UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(previewTouched:)];
                [imageView addGestureRecognizer:tapGesture];
                [tapGesture release];  
            }
        }
        
        if(UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
            imageView.center = CGPointMake(photosOffset.x + (maxPhotoSize.width + photosPadding)* i + maxPhotoSize.width / 2, photosOffset.y + (maxPhotoSize.height /2 ) );
        } else {
            imageView.center = CGPointMake(photosOffset.x + maxPhotoSize.width / 2, photosOffset.y + (maxPhotoSize.height + photosPadding) * i + maxPhotoSize.height / 2);
        }
    }
}

- (void) previewTouched: (UITapGestureRecognizer*) gesture { 
    if(self.thumbnailsView.userInteractionEnabled) {
        if ((gesture.view.tag-thumbnailsTagOffset)==self.review.reviewPhotoIndex) {
            return;
        }
        [self.thumbnailsView setUserInteractionEnabled:NO];
        [gesture.view setUserInteractionEnabled:NO];
        [self selectThumbnail:gesture.view];
        
    }
}

- (void) selectThumbnail:(UIView*) thumbnail {
     [self resetThumbnails];
    self.review.reviewPhotoIndex = thumbnail.tag - thumbnailsTagOffset;
    Photo*photo = [self.review.photos objectAtIndex:self.review.reviewPhotoIndex];
    photo.status = PhotoStatusSelected;
   

    [self setPhoto:(photo).image];
        self.btnDeleteThumbnail.hidden = YES ;    
    CGPoint oldCenter = thumbnail.center;
    [UIView animateWithDuration:.1 delay:0 options:UIViewAnimationCurveLinear animations:^{
        thumbnail.frame = CGRectMake(0, 0, thumbnail.frame.size.width * selectedScaleFactor, thumbnail.frame.size.height * selectedScaleFactor);
        thumbnail.layer.shadowPath = [UIBezierPath bezierPathWithRect:thumbnail.bounds].CGPath;
    } completion:^(BOOL finished){  
        self.thumbnailsView.userInteractionEnabled = YES;
        thumbnail.userInteractionEnabled = YES;
        self.btnDeleteThumbnail.userInteractionEnabled = YES;
        [self moveDeleteBotton];
        self.btnDeleteThumbnail.hidden = NO;
    }];
    thumbnail.center = oldCenter;
    [self.thumbnailsView bringSubviewToFront:thumbnail];
    [self.thumbnailsView bringSubviewToFront:self.btnDeleteThumbnail];
    
}

- (void) moveDeleteBotton {
    UIImageView* thumbnail  = [self thumbnailAtIndex:self.review.reviewPhotoIndex+thumbnailsTagOffset];
    self.btnDeleteThumbnail.center = CGPointMake(thumbnail.frame.origin.x + thumbnail.frame.size.width-10, thumbnail.frame.origin.y+10);

}

- (void) resetThumbnails {
    for(int i=0; i<self.review.photos.count; i++) {
        Photo *photo = [self.review.photos objectAtIndex:i];
        if(photo.status == PhotoStatusSelected) {
            UIImageView* thumbnail  = [self thumbnailAtIndex:i+thumbnailsTagOffset];
            photo.status = PhotoStatusActive;
            CGPoint center = thumbnail.center;
            [UIView animateWithDuration:.1 delay:0 options:UIViewAnimationCurveLinear animations:^{
                thumbnail.frame = CGRectMake(0, 0, thumbnail.frame.size.width / selectedScaleFactor, thumbnail.frame.size.height / selectedScaleFactor);
            } completion:^(BOOL finished){ 
                thumbnail.userInteractionEnabled = YES; 
            }];
            thumbnail.center = center;
            thumbnail.layer.shadowPath = [UIBezierPath bezierPathWithRect:thumbnail.bounds].CGPath;
        } 
    }
}

- (void) deletePhoto: (UIButton *) sender {
    NSInteger photoIndex =  self.review.reviewPhotoIndex;
    if ([self selectNewPhotoBeforeDeletingPhotoAtIndex: photoIndex]) {
        Photo* deletedPhoto = [self.review.photos objectAtIndex:photoIndex];
        deletedPhoto.status = PhotoStatusDeleted;
        UIImageView *thumbnail = [self thumbnailAtIndex:photoIndex+thumbnailsTagOffset];
        thumbnail.userInteractionEnabled = NO;
        for(UIGestureRecognizer *gesture in thumbnail.gestureRecognizers){
            [thumbnail removeGestureRecognizer:gesture];
        }
        [self dimThumbnail: thumbnail];
    }
}

- (UIImageView *) thumbnailAtIndex:(NSInteger) index {
    return (UIImageView *) [self.thumbnailsView viewWithTag:index];
}

- (BOOL) selectNewPhotoBeforeDeletingPhotoAtIndex:(NSUInteger) deletedPhotoIndex {
    for (NSUInteger i = (deletedPhotoIndex == self.review.photos.count - 1) ? 0 : deletedPhotoIndex + 1; 
         i != deletedPhotoIndex; 
         i = (i == self.review.photos.count - 1) ? 0 : i + 1) 
    {
        Photo* newPhoto = [self.review.photos objectAtIndex:i];
        if (newPhoto.status != PhotoStatusDeleted) {
            [self selectThumbnail:[self.thumbnailsView viewWithTag:i+thumbnailsTagOffset]];
            return YES;
        }
    }
    
    return NO;
}

- (void) dimThumbnail: (UIView*) thumbnail {    
    [UIView animateWithDuration:.3 delay:0 options:UIViewAnimationCurveLinear animations:^{
        thumbnail.alpha = deletedPhotoAlpha;
    } completion:nil];
        
}

- (UIImage*)imageWithBorderFromImage:(UIImage*)source {
    CGSize size = [source size];
    UIGraphicsBeginImageContext(size);
    CGRect rect = CGRectMake(0, 0, size.width, size.height);
    [source drawInRect:rect blendMode:kCGBlendModeNormal alpha:1.0];
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetRGBStrokeColor(context, 1.0, 1.0, 1.0, 1.0); 
    CGContextStrokeRectWithWidth(context, rect, 5.0);
    UIImage *testImg =  UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return testImg;
}

- (void) setPhoto: (UIImage *)photo {
    CGSize maxPhotoSize = CGSizeMake(666, 505);
    CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
    photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
    
    self.imageView.image = photo;
    
    CGPoint oldCenter = self.imageView.center;
    self.imageView.frame = CGRectMake(0, 0, photo.size.width, photo.size.height);
    self.imageView.center = oldCenter;
    self.imageView.layer.shadowPath = [UIBezierPath bezierPathWithRect:self.imageView.bounds].CGPath;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

- (void) willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval) duration {
    [self populateWithPhotos];
    if(UIInterfaceOrientationIsPortrait(toInterfaceOrientation)){
        self.view.backgroundColor  = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
        self.thumbnailsView.frame = CGRectMake(50, 830, 700, 230);
        self.imageView.center = CGPointMake(384, 348);
        self.submitButton.center = CGPointMake(384, 690);
    } else {
        self.view.backgroundColor = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
        self.thumbnailsView.frame = CGRectMake(50, 90, 230, 670);
        self.imageView.center = CGPointMake(622, 348);
        self.submitButton.center = CGPointMake(622, 674);
    }
    
}

- (void) didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self moveDeleteBotton];
}

#pragma mark - Actions

- (IBAction)shareThisPhoto:(id)sender {
    [self.rootController step];
}

@end
