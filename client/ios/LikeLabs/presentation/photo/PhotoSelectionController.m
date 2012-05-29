#import <AVFoundation/AVFoundation.h>
#import "PhotoSelectionController.h"
#import "RootController.h"
#import "RootPhotoController.h"
#import "Review.h"

@interface PhotoSelectionController ()
@property (nonatomic, retain) UIViewController <ContainerController> *rootController;
@property (nonatomic, assign) Review *review;
- (void) populateWithPhotos;
- (void) setPhoto: (UIImage *)photo;
- (void) resetThumbnails;
- (UIImage*)imageWithBorderFromImage:(UIImage*)source;
- (void) deletePhoto: (UIButton *) sender;
- (NSInteger)getIndexFrom:(NSInteger)infiniteScrollSectionIndex dataSize:(NSInteger)dataSize;
- (void) selectThumbnail:(UIView*) thumbnail;
- (void) dimThumbnail: (UIView*) thumbnail;
- (BOOL) selectNewPhotoBeforeDeletingPhotoAtIndex:(NSUInteger) deletedPhotoIndex;
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
NSInteger selectedIndex;

@synthesize imageView = _imageView;
@synthesize thumbnailsView = _thumbnailsView;
@synthesize submitButton = _submitButton;
@synthesize rootController = _rootController;
@synthesize review = _review;

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
    
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOpacity = 0.8;
    self.imageView.layer.shadowOffset = CGSizeMake(0, 5);
    self.imageView.layer.shadowRadius = 10;
    
}

#pragma mark - Memory management;

- (void)viewDidUnload
{
    [self setRootController:nil];
    [self setImageView:nil];
    [self setThumbnailsView:nil];

    [self setSubmitButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [_rootController release];
    [_imageView release];
    [_thumbnailsView release];
    [_submitButton release];
    [super dealloc];
}

#pragma mark - Photo management

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
        return 135;
    } else {
        return 119;
    }
}

-(NSInteger)getIndexFrom:(NSInteger)infiniteScrollSectionIndex dataSize:(NSInteger)dataSize {
    return infiniteScrollSectionIndex % dataSize;
}

- (void)populateWithPhotos {
    NSMutableArray *photos = self.review.photos;
    
    CGPoint photosOffset = CGPointMake(0, 0);
    CGSize maxPhotoSize = CGSizeMake(128.0, 114.0);
    int photosPadding = 10;

    for (int i=0; i<5; i++) {

        UIImageView *imageView = (UIImageView *) [self.thumbnailsView viewWithTag:(i+1)];
        if(!imageView) {
//            Photo* reviewPhoto = [[Photo alloc] initWithImage: [[UIImage imageNamed:@"thankyou_bg_landscape.png"] autorelease]];//[photos objectAtIndex:i];
            Photo* reviewPhoto = [photos objectAtIndex:i];
            UIImage *photo = reviewPhoto.image;
            CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
            NSLog(@"imageOrientation: %d", photo.imageOrientation);
            photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
            photo = [self imageWithBorderFromImage:photo];
            imageView = [[UIImageView alloc] initWithImage:photo];
            if (reviewPhoto.status == PhotoStatusDeleted) {
                [self dimThumbnail:imageView];
            }
        }
        if(UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
            imageView.center = CGPointMake(photosOffset.x + (maxPhotoSize.width + photosPadding)* i + maxPhotoSize.width / 2, photosOffset.y + (maxPhotoSize.height /2 ) );
        } else {
            imageView.center = CGPointMake(photosOffset.x + maxPhotoSize.width / 2, photosOffset.y + (maxPhotoSize.height + photosPadding) * i + maxPhotoSize.height / 2);
        }
        imageView.layer.shadowColor = [UIColor blackColor].CGColor;
        imageView.layer.shadowOffset = CGSizeMake(0, 6);
        imageView.layer.shadowOpacity = 0.75;
        imageView.layer.shadowRadius = 21;
        imageView.tag = i + 1;
        imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(previewTouched:)];
        [imageView addGestureRecognizer:tapGesture];
        [tapGesture release];        
        
        [self.thumbnailsView addSubview:imageView];
    }
    [self selectThumbnail:[self.thumbnailsView.subviews objectAtIndex:self.review.reviewPhotoIndex]];
}

- (void) previewTouched: (UITapGestureRecognizer*) gesture {   
    UIView* tappedImage = gesture.view;
    if (tappedImage.subviews.count > 0 || tappedImage.alpha == deletedPhotoAlpha) {
        return;
    }
    [self selectThumbnail:tappedImage];
}

- (void) selectThumbnail:(UIView*) thumbnail {
    UIView* tappedImage = thumbnail;
    [self resetThumbnails];
    [self setPhoto:((Photo*)[self.review.photos objectAtIndex:tappedImage.tag - 1]).image];
    //[self setPhoto:[[UIImage imageNamed:@"thankyou_bg_landscape.png"] autorelease]];
    self.review.reviewPhotoIndex = tappedImage.tag - 1;
    
    CGPoint oldCenter = tappedImage.center;
    tappedImage.frame = CGRectMake(0, 0, tappedImage.frame.size.width * selectedScaleFactor, tappedImage.frame.size.height * selectedScaleFactor);
    tappedImage.center = oldCenter;
    [self.thumbnailsView bringSubviewToFront:tappedImage];
    
    UIImage *btnImg = [UIImage imageNamed:@"delete_btn.png"];
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.tag = tappedImage.tag;
    [btn setImage:btnImg forState:UIControlStateNormal];
    btn.frame = CGRectMake(0, 0, btnImg.size.width, btnImg.size.height);
    CGFloat deleteBtnOffset = 10;
    btn.center = CGPointMake(tappedImage.frame.size.width - deleteBtnOffset, 0 + deleteBtnOffset);
    [btn addTarget:self action:@selector(deletePhoto:) forControlEvents:UIControlEventTouchDown];
    [tappedImage addSubview:btn];
}

- (void) resetThumbnails {
    for (UIImageView* thumbnail in self.thumbnailsView.subviews) {
        if (thumbnail.subviews.count > 0) {
            CGPoint center = thumbnail.center;
            thumbnail.frame = CGRectMake(0, 0, thumbnail.frame.size.width / selectedScaleFactor, thumbnail.frame.size.height / selectedScaleFactor);
            thumbnail.center = center;

            [thumbnail.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        }
    }
}

- (void) deletePhoto: (UIButton *) sender {
    if ([self selectNewPhotoBeforeDeletingPhotoAtIndex: sender.tag - 1]) {
        Photo* deletedPhoto = [self.review.photos objectAtIndex:sender.tag - 1];
        deletedPhoto.status = PhotoStatusDeleted;
        
        [self dimThumbnail:[self.thumbnailsView viewWithTag:sender.tag]];
        [sender removeFromSuperview];
    }
}

- (BOOL) selectNewPhotoBeforeDeletingPhotoAtIndex:(NSUInteger) deletedPhotoIndex {
    for (NSUInteger i = (deletedPhotoIndex == self.review.photos.count - 1) ? 0 : deletedPhotoIndex + 1; 
         i != deletedPhotoIndex; 
         i = (i == self.review.photos.count - 1) ? 0 : i + 1) 
    {
        Photo* newPhoto = [self.review.photos objectAtIndex:i];
        if (newPhoto.status != PhotoStatusDeleted) {
            [self selectThumbnail:[self.thumbnailsView viewWithTag:i+1]];
            return YES;
        }
    }
    return NO;
}

- (void) dimThumbnail: (UIView*) thumbnail {    
    thumbnail.alpha = deletedPhotoAlpha;    
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
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

-(void) willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
    [self populateWithPhotos];
    if(UIInterfaceOrientationIsPortrait(toInterfaceOrientation)){
        self.view.backgroundColor  = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
        self.thumbnailsView.frame = CGRectMake(50, 865, 700, 190);
        self.imageView.center = CGPointMake(384, 348);
        self.submitButton.center = CGPointMake(384, 690);
    } else {
        self.view.backgroundColor = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
        self.thumbnailsView.frame = CGRectMake(50, 130, 190, 670);
        self.imageView.center = CGPointMake(622, 348);
        self.submitButton.center = CGPointMake(622, 674);
    }

}

#pragma mark - Actions

- (IBAction)shareThisPhoto:(id)sender {
    [self.rootController step];
}

@end
