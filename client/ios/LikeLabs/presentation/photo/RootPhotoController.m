#import "RootPhotoController.h"
#import <AVFoundation/AVFoundation.h>

@interface RootPhotoController ()
@property (nonatomic, retain) CustomizableSegmentedControl* customSegmentedControl;
@property (nonatomic, retain) UIViewController* currentViewController;

- (NSMutableArray*) getButtons;
- (NSMutableDictionary*) getDividers;
- (void)setLabelsForInterfaceOrientation:(UIInterfaceOrientation)orientation;
@end

@implementation RootPhotoController
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
    CGRect frame;
    if(UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
        frame = CGRectMake(95, 24, 660, 43);
    } else {
        frame = CGRectMake(95, 24, 916, 43);      
    }
    self.customSegmentedControl = [[[CustomizableSegmentedControl alloc] initWithFrame:frame buttons:[self getButtons] widths:nil dividers:[self getDividers] dividerWidth:22 delegate:self] autorelease];
    [self.headerView addSubview:self.customSegmentedControl];

    UIViewController *vc = [RootController viewControllerByName:@"PhotoSelectionController" rootController:self];
    [self addChildViewController:vc];
    vc.view.frame = self.view.bounds;
    [self.view addSubview:vc.view];
    self.currentViewController = vc;
    
    [self setLabelsForInterfaceOrientation:[UIApplication sharedApplication].statusBarOrientation];
    [self.view bringSubviewToFront:self.headerView];
}

#pragma mark - Memory management

- (void)viewDidUnload
{
    [self setHeaderView:nil];
    [self setCustomSegmentedControl:nil];
    [self setNavigationBackground:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
    self.rootController = nil;
    self.currentViewController = nil;
    [_customSegmentedControl release];
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
    selectPhotoBtn.userInteractionEnabled = NO;
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
    NSString* enterInfoTitle = @"  Enter Your Information";
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
    NSString* finishedTitle = @"Finished!";
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

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self setLabelsForInterfaceOrientation:toInterfaceOrientation];
}

- (void)setLabelsForInterfaceOrientation:(UIInterfaceOrientation)orientation {
    for (UIButton* btn in self.customSegmentedControl.buttons) {
        btn.titleLabel.font = [UIFont systemFontOfSize:(UIInterfaceOrientationIsPortrait(orientation) ? 14 : 16)];
    }
    
    UIButton* selectPhotoBtn = [self.customSegmentedControl.buttons objectAtIndex:0];
    NSString* selectPhotoTitle = (UIInterfaceOrientationIsLandscape(orientation)) ? @"  Select Photo to Share" : @"  Select Photo";
    [selectPhotoBtn setTitle:selectPhotoTitle forState:UIControlStateNormal];
    [selectPhotoBtn setTitle:selectPhotoTitle forState:UIControlStateDisabled];
    [selectPhotoBtn setTitle:selectPhotoTitle forState:UIControlStateSelected];    
    
    UIButton* enterInfoBtn = [self.customSegmentedControl.buttons objectAtIndex:2];
    NSString* enterInfoTitle = (UIInterfaceOrientationIsLandscape(orientation)) ? @"  Enter Your Information" : @"  Enter Your Info";
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateNormal];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateDisabled];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateSelected];
    
    if(UIInterfaceOrientationIsPortrait(orientation)){
        self.customSegmentedControl.frame = CGRectMake(90, 24, 660, 43);
        self.navigationBackground.image = [UIImage imageNamed:NAVIGTION_BG_PORTRAIT];
    } else {
        self.customSegmentedControl.frame = CGRectMake(90, 24, 915, 43); 
        self.navigationBackground.image = [UIImage imageNamed:NAVIGTION_BG_LANDSCAPE];
    }
}

#pragma mark - CustomSegmentedControlDelegate implementation

- (void)selectedIndexChangedFrom:(NSUInteger)oldSegmentIndex to:(NSUInteger)newSegmentIndex setnder:(CustomizableSegmentedControl *)sender {
    UIButton* selectedBtn = [self.customSegmentedControl.buttons objectAtIndex:newSegmentIndex];
    selectedBtn.enabled = YES;
    [self.currentViewController resignFirstResponder];
    
    for (UIButton* btn in self.customSegmentedControl.buttons) {
        btn.userInteractionEnabled = (btn != selectedBtn);
    }
    if(oldSegmentIndex!=newSegmentIndex){
        NSString *controllerName = @"";
        switch (newSegmentIndex) {
            case 0:
                controllerName = @"PhotoSelectionController";
                break;
            case 1:
                controllerName = @"PhotoMessageController";
                break;
            case 2:
                controllerName = @"PhotoShareController";
                break;
            case 3:
                controllerName = @"PhotoFinishedController";
                self.customSegmentedControl.userInteractionEnabled = NO;
                break;
        }
        if(oldSegmentIndex<newSegmentIndex){
            [RootController switchToController:controllerName rootController:self];
        } else {
            [RootController switchBackToController:controllerName rootController:self];
        }
    }
}

#pragma mark - Actions

- (IBAction)goHome:(id)sender {
    [self.currentViewController resignFirstResponder];
    [self.rootController goHome];
}

- (void)step {
    self.customSegmentedControl.selectedSegmentIndex++;
}

- (Review *) getReview{
    return self.rootController.review;
}

- (ReviewService *) getReviewService{
    return self.rootController.reviewService;
}

- (UIViewController *) getCurrentController{
    return self.currentViewController;
}

- (void) setCurrentController:(UIViewController *)controller{
    self.currentViewController = controller;
}

-(void)bringHeaderViewToFront{
    [self.view bringSubviewToFront:self.headerView];
}


@end
