#import <AVFoundation/AVFoundation.h>
#import "PhotoSelectionController.h"

@interface PhotoSelectionController ()
@property (nonatomic, retain) RootController *rootController;
- (void) populateWithPhotos;
- (void) setPhoto: (UIImage *)photo;
- (void) resetThumbNails;
- (UIImage*)imageWithBorderFromImage:(UIImage*)source;
- (void) deletePhoto2: (UIButton *) sender;
@end

@implementation PhotoSelectionController

static NSString *const bgLandscape = @"photo_selection_bg.png";
static NSString *const bgPortrait = @"photo_selection_bg_portrait.png";
static NSString *const NAVIGATION_BACKGROUND_IMG = @"navigation_bg.png";
static NSString *const NAV_BTN_NORMAL_IMG = @"navigation_button_normal.png";
static NSString *const NAV_BTN_SELECTED_IMG = @"navigation_button_selected.png";
static NSString *const NAV_DIVIDER_NN_IMG = @"navigation_divider_nn.png";
static NSString *const NAV_DIVIDER_SN_IMG = @"navigation_divider_sn.png";
static NSString *const NAV_DIVIDER_NS_IMG = @"navigation_divider_ns.png";
const float selectedScaleFactor = 1.5;

@synthesize segmentedControl = _segmentedControl;
@synthesize navigationBackground = _navigationBackground;
@synthesize imageView = _imageView;
@synthesize thumbNailsView = _thumbNailsView;
@synthesize rootController = _rootController;

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    
    self.navigationBackground.image = [[UIImage imageNamed:NAVIGATION_BACKGROUND_IMG] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    self.navigationBackground.contentMode = UIViewContentModeScaleToFill;
    
    [self.segmentedControl setBackgroundImage:[UIImage imageNamed:NAV_BTN_NORMAL_IMG] forState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setBackgroundImage:[UIImage imageNamed:NAV_BTN_SELECTED_IMG] forState:UIControlStateSelected barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_NN_IMG] 
                       forLeftSegmentState:UIControlStateNormal rightSegmentState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_SN_IMG] 
                       forLeftSegmentState:UIControlStateSelected rightSegmentState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
    [self.segmentedControl setDividerImage:[UIImage imageNamed:NAV_DIVIDER_NS_IMG] 
                       forLeftSegmentState:UIControlStateNormal rightSegmentState:UIControlStateSelected barMetrics:UIBarMetricsDefault];    
    [self.segmentedControl setEnabled:NO forSegmentAtIndex:3];

    UIColor *background = [[UIColor alloc] initWithPatternImage:
                           [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.view.backgroundColor = background;
    [background release];
    [self populateWithPhotos];
    
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOpacity = 0.8;
    self.imageView.layer.shadowOffset = CGSizeMake(0, 5);
    self.imageView.layer.shadowRadius = 10;
}

- (void)populateWithPhotos {
    NSMutableArray *photos = self.rootController.review.photos;
    CGPoint photosOffset = CGPointMake(0, 0);
    CGSize maxPhotoSize = CGSizeMake(128.0, 114.0);
    int photosPadding = 10;
    for (int i=0; i<5; i++) {
        UIImage *photo = [photos objectAtIndex:i];
        CGFloat scale = MIN(maxPhotoSize.width / photo.size.width, maxPhotoSize.height / photo.size.height);
        photo = [UIImage imageWithCGImage:photo.CGImage scale:1.0/scale orientation:photo.imageOrientation];
        photo = [self imageWithBorderFromImage:photo];
        UIImageView *imageView = [[UIImageView alloc] initWithImage:photo];
        imageView.center = CGPointMake(photosOffset.x + maxPhotoSize.width / 2, photosOffset.y + (maxPhotoSize.height + photosPadding) * i + maxPhotoSize.height / 2);
        imageView.layer.shadowColor = [UIColor blackColor].CGColor;
        imageView.layer.shadowOffset = CGSizeMake(0, 6);
        imageView.layer.shadowOpacity = 0.75;
        imageView.layer.shadowRadius = 21;
        imageView.tag = i + 1;
        imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(previewTouched:)];
        [imageView addGestureRecognizer:tapGesture];
        [tapGesture release];
        [self.thumbNailsView addSubview:imageView];
    }
    [self setPhoto:[self.rootController.review.photos objectAtIndex:self.rootController.review.reviewPhotoIndex]];
}

- (void) previewTouched: (UITapGestureRecognizer*) gesture {   
    UIView* tappedImage = gesture.view;
    if (tappedImage.subviews.count > 0 || tappedImage.alpha == 0.5) {
        return;
    }
    
    [self resetThumbNails];
    [self setPhoto:[self.rootController.review.photos objectAtIndex:tappedImage.tag - 1]];
    self.rootController.review.reviewPhotoIndex = tappedImage.tag - 1;        
    
    CGPoint oldCenter = tappedImage.center;
    tappedImage.frame = CGRectMake(0, 0, tappedImage.frame.size.width * selectedScaleFactor, tappedImage.frame.size.height * selectedScaleFactor);
    tappedImage.center = oldCenter;
    
    UIImage *btnImg = [UIImage imageNamed:@"delete_btn.png"];
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.tag = tappedImage.tag;
    [btn setImage:btnImg forState:UIControlStateNormal];
    btn.frame = CGRectMake(0, 0, btnImg.size.width, btnImg.size.height); //FIXME scaleToFit
    btn.center = CGPointMake(tappedImage.frame.size.width - 10, 0 + 10);
    [btn addTarget:self action:@selector(deletePhoto2:) forControlEvents:UIControlEventTouchDown];
    [tappedImage addSubview:btn];
}

- (void) resetThumbNails {
    for (UIImageView* thumbnail in self.thumbNailsView.subviews) {
        if (thumbnail.subviews.count > 0) {
            CGPoint center = thumbnail.center;
            thumbnail.frame = CGRectMake(0, 0, thumbnail.frame.size.width / selectedScaleFactor, thumbnail.frame.size.height / selectedScaleFactor);
            thumbnail.center = center;

            [thumbnail.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        }
    }
}

- (void) deletePhoto2: (UIButton *) sender {
    UIView *thumbnail = [self.thumbNailsView viewWithTag:sender.tag];
    CGPoint center = thumbnail.center;
    thumbnail.frame = CGRectMake(0, 0, thumbnail.frame.size.width / selectedScaleFactor, thumbnail.frame.size.height / selectedScaleFactor);
    thumbnail.center = center;
    thumbnail.alpha = 0.5;
    [sender removeFromSuperview];
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

- (void)viewDidUnload
{
    [self setSegmentedControl:nil];
    [self setRootController:nil];
    [self setNavigationBackground:nil];
    [self setImageView:nil];
    [self setThumbNailsView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

//-(void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
//    UIColor *background;    
//    if (toInterfaceOrientation == UIInterfaceOrientationPortrait || toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) {
//        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
//    } else {
//        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
//    }
//    self.view.backgroundColor = background;
//}

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}

- (void)dealloc {
    [_segmentedControl release];
    [_rootController release];
    [_navigationBackground release];
    [_imageView release];
    [_thumbNailsView release];
    [super dealloc];
}

- (IBAction)shareThisPhoto:(id)sender {
    [self.rootController switchToController:@"PhotoMessageController"];
}

- (IBAction)navigationChanged:(UISegmentedControl *)sender {
    if (sender.selectedSegmentIndex == 1) {
        [self shareThisPhoto:sender];
    }
}

@end
