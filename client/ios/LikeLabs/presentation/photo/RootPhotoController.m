#import "RootPhotoController.h"
#import <AVFoundation/AVFoundation.h>

@interface RootPhotoController ()
@property (nonatomic, retain) CustomizableSegmentedControl* customSegmentedControl;
@property (nonatomic, retain) UIViewController* currentViewController;
@end

@implementation RootPhotoController
@synthesize segmentedControl = _segmentedControl;
@synthesize headerView = _headerView;
@synthesize navigationBackground = _navigationBackground;
@synthesize rootController = _rootController;
@synthesize customSegmentedControl = _customSegmentedControl;
@synthesize currentViewController = _currentViewController;

#pragma mark - Initialization

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
    
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.navigationBackground.image = [[UIImage imageNamed:@"navigation_bg.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    self.navigationBackground.contentMode = UIViewContentModeScaleToFill;
    self.segmentedControl.alpha = 0;
    _customSegmentedControl = [[CustomizableSegmentedControl alloc] initWithFrame:self.segmentedControl.frame buttons:[self getButtons] widths:nil dividers:[self getDividers] dividerWidth:22 delegate:self];
    self.customSegmentedControl.autoresizingMask = self.segmentedControl.autoresizingMask;
    [self.headerView addSubview:self.customSegmentedControl];
    
    UIViewController *vc = [self viewControllerByName:@"PhotoSelectionController"];
    [self addChildViewController:vc];
    vc.view.frame = self.view.bounds;
    [self.view addSubview:vc.view];
    self.currentViewController = vc;
    
    [self.view bringSubviewToFront:self.headerView];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setSegmentedControl:nil];
    [self setHeaderView:nil];
    [self setCustomSegmentedControl:nil];
    [self setNavigationBackground:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    [_customSegmentedControl release];
    [_segmentedControl release];
    [_headerView release];
    [_navigationBackground release];
    [super dealloc];
}

#pragma mark - CustomSegmentedControl population

- (NSMutableArray*) getButtons {
    NSMutableArray* bt2 = [[[NSMutableArray alloc] initWithCapacity:4] autorelease];
    UIButton* selectPhotoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* firstImgSelected = [[UIImage imageNamed:@"btn_first_bg_selected.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* firstImgNormal = [[UIImage imageNamed:@"btn_first_bg_normal.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* oneImg = [UIImage imageNamed:@"1.png"];
    [selectPhotoBtn setBackgroundImage: firstImgNormal forState:UIControlStateNormal];
    [selectPhotoBtn setBackgroundImage:firstImgSelected forState:UIControlStateSelected];
    [selectPhotoBtn setImage:oneImg forState:UIControlStateSelected];
    [selectPhotoBtn setImage:oneImg forState:UIControlStateNormal];
    NSString* selectTitle = @"  Select Photo to Share";
    [selectPhotoBtn setTitle:selectTitle forState:UIControlStateNormal];
    [selectPhotoBtn setTitle:selectTitle forState:UIControlStateSelected];
    [bt2 addObject:selectPhotoBtn];
    
    
    UIButton* writeMsgBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* selectedImg = [[UIImage imageNamed:@"btn_bg_selected.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* normalImg = [[UIImage imageNamed:@"btn_bg_normal.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* disabledImg = [[UIImage imageNamed:@"btn_bg_disabled.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* twoImg = [UIImage imageNamed:@"2.png"];
    [writeMsgBtn setBackgroundImage:selectedImg forState:UIControlStateSelected];
    [writeMsgBtn setBackgroundImage:normalImg forState:UIControlStateNormal];
    [writeMsgBtn setBackgroundImage:disabledImg forState:UIControlStateDisabled];
    [writeMsgBtn setImage:twoImg forState:UIControlStateNormal];
    [writeMsgBtn setImage:twoImg forState:UIControlStateDisabled];
    [writeMsgBtn setImage:twoImg forState:UIControlStateSelected];
    NSString* writeMsgTitle = @"  Write A Message";
    [writeMsgBtn setTitle:writeMsgTitle forState:UIControlStateNormal];
    [writeMsgBtn setTitle:writeMsgTitle forState:UIControlStateSelected];
    [writeMsgBtn setTitle:writeMsgTitle forState:UIControlStateDisabled];
    [bt2 addObject:writeMsgBtn];
    
    
    UIButton* enterInfoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [enterInfoBtn setBackgroundImage:selectedImg forState:UIControlStateSelected];
    [enterInfoBtn setBackgroundImage:normalImg forState:UIControlStateNormal];
    [enterInfoBtn setBackgroundImage:disabledImg forState:UIControlStateDisabled];
    UIImage* threeImg = [UIImage imageNamed:@"3.png"];
    [enterInfoBtn setImage:threeImg forState:UIControlStateNormal];
    [enterInfoBtn setImage:threeImg forState:UIControlStateDisabled];
    [enterInfoBtn setImage:threeImg forState:UIControlStateSelected];
    NSString* enterInfoTitle = @"  Enter your information";
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateNormal];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateSelected];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateDisabled];
    [bt2 addObject:enterInfoBtn];
    
    UIButton* finishedBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* finishedDisabledImg = [[UIImage imageNamed:@"btn_last_bg_disabled.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* finishedSelectedImg = [[UIImage imageNamed:@"btn_last_bg_selected.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* finishedNormalImg = [[UIImage imageNamed:@"btn_last_bg_normal.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    [finishedBtn setBackgroundImage:finishedSelectedImg forState:UIControlStateSelected];
    [finishedBtn setBackgroundImage:finishedNormalImg forState:UIControlStateNormal];
    [finishedBtn setBackgroundImage:finishedDisabledImg forState:UIControlStateDisabled];
    NSString* finishedTitle = @"Finished";
    [finishedBtn setTitle:finishedTitle forState:UIControlStateNormal];
    [finishedBtn setTitle:finishedTitle forState:UIControlStateSelected];
    [finishedBtn setTitle:finishedTitle forState:UIControlStateDisabled];
    [bt2 addObject:finishedBtn];    
    
    UIColor* whiteColor = [UIColor whiteColor];
    for (NSUInteger i=0; i<bt2.count; i++) {
        UIButton* btn = [bt2 objectAtIndex:i];
        btn.enabled = btn.selected = (i==0);
        
        [btn setTitleColor:whiteColor forState:UIControlStateNormal];
        [btn setTitleColor:whiteColor forState:UIControlStateSelected];
        [btn setTitleColor:whiteColor forState:UIControlStateDisabled];
    }
    return bt2;
}

- (NSMutableDictionary*) getDividers {
    NSMutableDictionary* div2 = [[[NSMutableDictionary alloc] initWithCapacity:3] autorelease];
    
    NSMutableDictionary* ln = [[NSMutableDictionary alloc] initWithCapacity:3];
    [ln setObject:[UIImage imageNamed:@"divider_nn.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateNormal]];
    [ln setObject:[UIImage imageNamed:@"divider_ns.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateSelected]];
    [ln setObject:[UIImage imageNamed:@"divider_nd.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];
    [div2 setObject:ln forKey:[NSNumber numberWithUnsignedInt:UIControlStateNormal]];
    [ln release];
    
    NSMutableDictionary* ls = [[NSMutableDictionary alloc] initWithCapacity:2];
    [ls setObject:[UIImage imageNamed:@"divider_sd.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];
    [ls setObject:[UIImage imageNamed:@"divider_sn.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateNormal]];
    [div2 setObject:ls forKey:[NSNumber numberWithUnsignedInt:UIControlStateSelected]];
    [ls release];
    
    NSMutableDictionary* ld = [[NSMutableDictionary alloc] initWithCapacity:1];
    [ld setObject:[UIImage imageNamed:@"divider_dd.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];    
    [div2 setObject:ld forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];
    [ld release];
    return div2;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return YES;
}

#pragma mark - ContainerController implementation

- (UIViewController *)viewControllerByName:(NSString *)controllerName {
    return [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:self] autorelease];
}

- (void)switchToController:(NSString *)controllerName {
    UIViewController *vc = [self viewControllerByName:controllerName];
    [self addChildViewController:vc];
    [self transitionFromViewController:self.currentViewController toViewController:vc duration:0.5 options:UIViewAnimationOptionTransitionFlipFromLeft animations: ^{
        [self.currentViewController.view removeFromSuperview];
        vc.view.frame = self.view.bounds;
        [self.view addSubview:vc.view];
        [self.view bringSubviewToFront:self.headerView];
    } completion:^(BOOL finished) {
        [vc didMoveToParentViewController:self];
        [self.currentViewController removeFromParentViewController];
        self.currentViewController = vc;
    }];
}

#pragma mark - CustomSegmentedControlDelegate implementation

- (void)selectedIndexChangedFrom:(NSUInteger)oldSegmentIndex to:(NSUInteger)newSegmentIndex setnder:(CustomizableSegmentedControl *)sender {
    UIButton* selectedBtn = [self.customSegmentedControl.buttons objectAtIndex:newSegmentIndex];
    selectedBtn.enabled = YES;
    
    switch (newSegmentIndex) {
        case 0:
            [self switchToController:@"PhotoSelectionController"];
            break;
        case 1:
            [self switchToController:@"PhotoMessageController"];
            break;
        case 2:
            [self switchToController:@"PhotoShareController"];
            break;
        case 3:
            [self switchToController:@"PhotoFinishedController"];
            break;
    }
}

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}

- (void)step {
    self.customSegmentedControl.selectedSegmentIndex++;
}

@end
