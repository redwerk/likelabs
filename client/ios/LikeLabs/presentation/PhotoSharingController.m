#import "PhotoSharingController.h"

@interface PhotoSharingController ()
@property (nonatomic, retain) RootController* rootController;
@property (nonatomic, retain) CustomizableSegmentedControl* customSegmentedControl;
@end

@implementation PhotoSharingController
@synthesize segmentedControl = _segmentedControl;
@synthesize headerView = _headerView;
@synthesize rootController = _rootController;
@synthesize customSegmentedControl = _customSegmentedControl;

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
    
    self.segmentedControl.alpha = 0;
    self.customSegmentedControl = [[CustomizableSegmentedControl alloc] initWithFrame:self.segmentedControl.frame buttons:[self getButtons] widths:nil dividers:[self getDividers] dividerWidth:22 delegate:self];
    [self.headerView addSubview:self.customSegmentedControl];
}

- (NSMutableArray*) getButtons {
    NSMutableArray* bt2 = [[NSMutableArray alloc] initWithCapacity:4];
    UIButton* selectPhotoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* firstImgSelected = [[UIImage imageNamed:@"btn_first_bg_selected.png"] stretchableImageWithLeftCapWidth:20 topCapHeight:0];
    UIImage* firstImgNormal = [[UIImage imageNamed:@"btn_first_bg_normal.png"] stretchableImageWithLeftCapWidth:20 topCapHeight:0];
    UIImage* oneImg = [UIImage imageNamed:@"1.png"];
    [selectPhotoBtn setBackgroundImage: firstImgNormal forState:UIControlStateNormal];
    [selectPhotoBtn setBackgroundImage:firstImgSelected forState:UIControlStateSelected];
    [selectPhotoBtn setImage:oneImg forState:UIControlStateSelected];
    [selectPhotoBtn setImage:oneImg forState:UIControlStateNormal];
    NSString* selectTitle = @"  Select Photo to Share";
    [selectPhotoBtn setTitle:selectTitle forState:UIControlStateNormal];
    [selectPhotoBtn setTitle:selectTitle forState:UIControlStateSelected];
    UIColor* whiteColor = [UIColor whiteColor];
    [selectPhotoBtn setTitleColor:whiteColor forState:UIControlStateNormal];
    [selectPhotoBtn setTitleColor:whiteColor forState:UIControlStateSelected];
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
    [writeMsgBtn setTitleColor:whiteColor forState:UIControlStateNormal];
    [writeMsgBtn setTitleColor:whiteColor forState:UIControlStateSelected];
    [writeMsgBtn setTitleColor:whiteColor forState:UIControlStateDisabled];
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
    [enterInfoBtn setTitleColor:whiteColor forState:UIControlStateNormal];
    [enterInfoBtn setTitleColor:whiteColor forState:UIControlStateSelected];
    [enterInfoBtn setTitleColor:whiteColor forState:UIControlStateDisabled];
    [bt2 addObject:enterInfoBtn];
    
    UIButton* finishedBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage* finishedDisabledImg = [[UIImage imageNamed:@"btn_last_bg_disabled.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* finishedSelectedImg = [[UIImage imageNamed:@"btn_last_bg_selected.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    UIImage* finishedNormalImg = [[UIImage imageNamed:@"btn_last_bg_normal.png"] stretchableImageWithLeftCapWidth:25 topCapHeight:0];
    [finishedBtn setBackgroundImage:finishedSelectedImg forState:UIControlStateSelected];
    [finishedBtn setBackgroundImage:finishedNormalImg forState:UIControlStateNormal];
    [finishedBtn setBackgroundImage:finishedDisabledImg forState:UIControlStateDisabled];
    NSString* finishedTitle = @"  Finished";
    [finishedBtn setTitle:finishedTitle forState:UIControlStateNormal];
    [finishedBtn setTitle:finishedTitle forState:UIControlStateSelected];
    [finishedBtn setTitle:finishedTitle forState:UIControlStateDisabled];
    [finishedBtn setTitleColor:whiteColor forState:UIControlStateNormal];
    [finishedBtn setTitleColor:whiteColor forState:UIControlStateSelected];
    [finishedBtn setTitleColor:whiteColor forState:UIControlStateDisabled];
    [bt2 addObject:finishedBtn];    
    return bt2;
}

- (NSMutableDictionary*) getDividers {
    NSMutableDictionary* div2 = [[[NSMutableDictionary alloc] initWithCapacity:3] autorelease];
    
    NSMutableDictionary* ln = [[NSMutableDictionary alloc] initWithCapacity:2];
    [ln setObject:[UIImage imageNamed:@"divider_nn.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateNormal]];
    [ln setObject:[UIImage imageNamed:@"divider_ns.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateSelected]];
    [div2 setObject:ln forKey:[NSNumber numberWithUnsignedInt:UIControlStateNormal]];
    
    NSMutableDictionary* ls = [[NSMutableDictionary alloc] initWithCapacity:1];
    [ls setObject:[UIImage imageNamed:@"divider_sd.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];
    [div2 setObject:ls forKey:[NSNumber numberWithUnsignedInt:UIControlStateSelected]];
    
    NSMutableDictionary* ld = [[NSMutableDictionary alloc] initWithCapacity:1];
    [ld setObject:[UIImage imageNamed:@"divider_dd.png"] forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];    
    [div2 setObject:ld forKey:[NSNumber numberWithUnsignedInt:UIControlStateDisabled]];
    return div2;
}

- (void)viewDidUnload
{
    [self setSegmentedControl:nil];
    [self setHeaderView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft || interfaceOrientation == UIInterfaceOrientationLandscapeRight);
}

- (IBAction)goHome:(id)sender {
    [self.rootController switchToController:@"SplashScreenController"];
}

- (void)dealloc {
    [_segmentedControl release];
    [_headerView release];
    [super dealloc];
}
@end
