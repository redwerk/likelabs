#import "RootMessageController.h"
#import <AVFoundation/AVFoundation.h>

@interface RootMessageController ()
@property (nonatomic, retain) CustomizableSegmentedControl* customSegmentedControl;
@property (nonatomic, retain) UIViewController* currentViewController;
@property (nonatomic, retain) Review *review;
- (void)setLabelsForInterfaceOrientation:(UIInterfaceOrientation)orientation;
- (UIViewController *)viewControllerByName:(NSString *)controllerName;

@end

@implementation RootMessageController
@synthesize headerView = _headerView;
@synthesize navigationBackground = _navigationBackground;
@synthesize rootController = _rootController;
@synthesize customSegmentedControl = _customSegmentedControl;
@synthesize currentViewController = _currentViewController;
@synthesize review = _review;

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        self.rootController.review = [[[Review alloc] initWithReviewType:ReviewTypeText] autorelease];
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.navigationBackground.contentMode = UIViewContentModeScaleToFill;
    self.customSegmentedControl.alpha = 0;
    CGRect frame;
    if(UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
        frame = CGRectMake(95, 24, 660, 43);
    } else {
        frame = CGRectMake(95, 24, 916, 43);      
    }
    self.customSegmentedControl = [[[CustomizableSegmentedControl alloc] initWithFrame:frame buttons:[self getButtons] widths:nil dividers:[self getDividers] dividerWidth:22 delegate:self] autorelease];

    [self.headerView addSubview:self.customSegmentedControl];
    
    [RootController switchToController:@"TextReviewController" rootController:self];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (NSMutableArray*) getButtons {
    NSMutableArray* bt2 = [[[NSMutableArray alloc] initWithCapacity:4] autorelease];
    UIButton* writeMessageBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* firstImgSelected = [[UIImage imageNamed:@"btn_first_bg_selected.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* firstImgNormal = [[UIImage imageNamed:@"btn_first_bg_normal.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* oneImg = [UIImage imageNamed:@"1.png"];
    [writeMessageBtn setBackgroundImage: firstImgNormal forState:UIControlStateNormal];
    [writeMessageBtn setBackgroundImage:firstImgSelected forState:UIControlStateSelected];
    [writeMessageBtn setImage:oneImg forState:UIControlStateSelected];
    [writeMessageBtn setImage:oneImg forState:UIControlStateNormal];
    NSString* writeMsgTitle = @"  Write A Message";
    [writeMessageBtn setTitle:writeMsgTitle forState:UIControlStateNormal];
    [writeMessageBtn setTitle:writeMsgTitle forState:UIControlStateSelected];
    [bt2 addObject:writeMessageBtn];
    
    
    UIButton* infoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* selectedImg = [[UIImage imageNamed:@"btn_bg_selected.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* normalImg = [[UIImage imageNamed:@"btn_bg_normal.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* disabledImg = [[UIImage imageNamed:@"btn_bg_disabled.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:0];
    UIImage* twoImg = [UIImage imageNamed:@"2.png"];
    [infoBtn setBackgroundImage:selectedImg forState:UIControlStateSelected];
    [infoBtn setBackgroundImage:normalImg forState:UIControlStateNormal];
    [infoBtn setBackgroundImage:disabledImg forState:UIControlStateDisabled];
    [infoBtn setImage:twoImg forState:UIControlStateNormal];
    [infoBtn setImage:twoImg forState:UIControlStateDisabled];
    [infoBtn setImage:twoImg forState:UIControlStateSelected];
    NSString* infoTitle = @"  Enter Your Information";
    [infoBtn setTitle:infoTitle forState:UIControlStateNormal];
    [infoBtn setTitle:infoTitle forState:UIControlStateSelected];
    [infoBtn setTitle:infoTitle forState:UIControlStateDisabled];
    [bt2 addObject:infoBtn];
    
    
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

- (void)setLabelsForInterfaceOrientation:(UIInterfaceOrientation)orientation {
  
    UIButton* enterInfoBtn = [self.customSegmentedControl.buttons objectAtIndex:1];
    NSString* enterInfoTitle = (UIInterfaceOrientationIsLandscape(orientation)) ? @"  Enter Your Information" : @"  Enter Your Info";
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateNormal];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateDisabled];
    [enterInfoBtn setTitle:enterInfoTitle forState:UIControlStateSelected];

    if(UIInterfaceOrientationIsPortrait(orientation)){
        self.customSegmentedControl.frame = CGRectMake(90, 24, 661, 43);
        self.navigationBackground.image = [UIImage imageNamed:NAVIGTION_BG_PORTRAIT];
    } else {
        self.customSegmentedControl.frame = CGRectMake(90, 24, 916, 43); 
        self.navigationBackground.image = [UIImage imageNamed:NAVIGTION_BG_LANDSCAPE];
    }
}

-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
    [self setLabelsForInterfaceOrientation:toInterfaceOrientation];

}

#pragma mark - ContainerController implementation

- (UIViewController *)viewControllerByName:(NSString *)controllerName {
    return [[(UIViewController<ChildController> *)[NSClassFromString(controllerName) alloc] initWithRootController:self] autorelease];
}

#pragma mark - CustomSegmentedControlDelegate implementation


- (void)selectedIndexChangedFrom:(NSUInteger)oldSegmentIndex to:(NSUInteger)newSegmentIndex setnder:(CustomizableSegmentedControl *)sender {
    UIButton* selectedBtn = [self.customSegmentedControl.buttons objectAtIndex:newSegmentIndex];
    selectedBtn.enabled = YES;
    [self.currentViewController resignFirstResponder];
    for (UIButton* btn in self.customSegmentedControl.buttons) {
        btn.userInteractionEnabled = (btn != selectedBtn);
    }
    NSString *controllerName = @"";
    
    if(oldSegmentIndex!=newSegmentIndex){
        switch (newSegmentIndex) {
            case 0:
                controllerName=@"TextReviewController";
                break;
            case 1:
                controllerName=@"PhotoShareController";
                break;
            case 2:
                controllerName=@"PhotoFinishedController";
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
    [RootController switchBackToController:@"SplashScreenController" rootController:self.rootController];
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
