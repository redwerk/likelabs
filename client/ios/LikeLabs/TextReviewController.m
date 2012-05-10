#import <QuartzCore/QuartzCore.h>
#import "TextReviewController.h"
#import "SettingsDao.h"
#import "Review.h"
#import "User.h"
#import "RootController.h"

static NSString *const GREETING = @"Start typing a message!";
static NSString *const TEXT_CELL_IDENTIFIER = @"textCellIdeintifier";
static NSString *const bgLandscape = @"bg_landscape.png";
static NSString *const bgPortrait = @"bg_portrait.png";


@interface TextReviewController()
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) NSArray* reviews;
@property (retain, nonatomic) NSTimer* timer;
@property (assign) BOOL textPlaseholderActive;
- (CGFloat) getTextHeight:(NSString*) text font:(UIFont*) font;
- (NSInteger) getIndexFrom: (NSInteger)infiniteScrollSectionIndex dataSize: (NSInteger) dataSize;
- (void) scrollComments;
@end

@implementation TextReviewController

const float BORDER_COLOR = 0.8f;
const float BORDER_WIDTH = 2.0f;
const float BORDER_CORNER_RADIUS = 10.0f;
const float FONT_SIZE = 14.0f;
const float CELL_CONTENT_MARGIN = 10.0f;
const float CELL_CONTENT_MARGIN_WIDTH = 10.0f;
const float MIN_TEXT_HEIGHT = 44.0f;
const float ANIMATION_DURATION = 0.02;
const int ANIMATION_SPEED = 1;
const int ROWS_ENDLESS = 1000;

float commentsContentOffset = 0;

@synthesize socialComments = _socialComments;
@synthesize textView = _textView;
@synthesize rootController = _rootController;
@synthesize reviews = _reviews;
@synthesize timer = _timer;
@synthesize textPlaseholderActive = _textPlaseholderActive;

- (id)initWithRootController:(RootController *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        SettingsDao* dao = [[SettingsDao alloc] init];
        self.reviews = [dao getTextReviews];
        [dao release];
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

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    _timer = [NSTimer scheduledTimerWithTimeInterval:ANIMATION_DURATION target:self selector:@selector(scrollComments) userInfo:nil repeats:YES];
    commentsContentOffset = 0;
    [self scrollComments];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    [self.socialComments setBackgroundView:nil];
    [self.socialComments setBackgroundView:[[[UIView alloc] init] autorelease]];
    [self.socialComments setBackgroundColor:[UIColor clearColor]];
    
    
    UIColor *background = [[UIColor alloc] initWithPatternImage:
                           [UIImage imageNamed:!UIDeviceOrientationIsPortrait([UIDevice currentDevice].orientation) ? bgLandscape : bgPortrait]];
    self.view.backgroundColor = background;
    [background release];

    self.textView.layer.borderColor = [[UIColor colorWithWhite:BORDER_COLOR alpha:1.0] CGColor];
    self.textView.layer.borderWidth = BORDER_WIDTH;
    self.textView.layer.cornerRadius = BORDER_CORNER_RADIUS;    
    self.textPlaseholderActive = true;
    [self.textView becomeFirstResponder];    
}

- (void) scrollComments {   
     commentsContentOffset +=ANIMATION_SPEED;
     [self.socialComments setContentOffset: CGPointMake(0, commentsContentOffset) animated:NO];
}

- (void)viewDidUnload
{
    [self setSocialComments:nil];
    [self setTextView:nil];
    [self setRootController:nil];
    [self setReviews:nil];
    [super viewDidUnload];    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
	return YES;
}

- (BOOL)textView:(UITextView *)view shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (self.textPlaseholderActive) {
        self.textView.text = @"";
        self.textView.textColor = [UIColor blackColor];
        self.textPlaseholderActive = false;
    } 
    return YES;
}

- (void)textViewDidChange:(UITextView* ) view {
    if (self.textView.text.length == 0) {
        self.textView.textColor = [UIColor lightGrayColor];
        self.textView.text = GREETING;
        self.textPlaseholderActive = true;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return ROWS_ENDLESS;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger i = [self getIndexFrom:indexPath.section dataSize:self.reviews.count];
    UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:TEXT_CELL_IDENTIFIER];
    UILabel* label = nil;
    UILabel* titleLabel = nil;
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:TEXT_CELL_IDENTIFIER] autorelease];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        label = [[UILabel alloc] initWithFrame:CGRectZero];
        [label setLineBreakMode:UILineBreakModeWordWrap];
        [label setMinimumFontSize:FONT_SIZE];
        [label setNumberOfLines:0];
        [label setFont:[UIFont systemFontOfSize:FONT_SIZE]];
        [label setBackgroundColor:[UIColor clearColor]];
        [label setTag:1];
        
        titleLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        [titleLabel setLineBreakMode:UILineBreakModeWordWrap];
        [titleLabel setMinimumFontSize:FONT_SIZE];
        [titleLabel setNumberOfLines:0];
        [titleLabel setFont:[UIFont boldSystemFontOfSize:FONT_SIZE]];
        [titleLabel setBackgroundColor:[UIColor clearColor]];
        [titleLabel setTag:2];
        
        [[cell contentView] addSubview:label];         
        [[cell contentView] addSubview:titleLabel];
        [label release];
        [titleLabel release];
    }         
    if (!label) {
        label = (UILabel*)[cell viewWithTag:1];
    }
    if (!titleLabel) {
        titleLabel = (UILabel*)[cell viewWithTag:2];
    }
    
    Review* review = [self.reviews objectAtIndex:i];

    CGFloat textHeight = [self getTextHeight:review.text font:[UIFont systemFontOfSize:FONT_SIZE]];
    CGFloat titleHeight = [self getTextHeight:review.user.name font:[UIFont boldSystemFontOfSize:FONT_SIZE]];
    
    CGFloat CELL_CONTENT_WIDTH = self.socialComments.frame.size.width - (CELL_CONTENT_MARGIN_WIDTH*2);
    
    [label setText:review.text];
    [label setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN + titleHeight, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH * 2), MAX(textHeight, MIN_TEXT_HEIGHT))];
    
    [titleLabel setText:review.user.name];
    [titleLabel setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH*2), titleHeight)];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    Review* review = [self.reviews objectAtIndex:[self getIndexFrom:indexPath.section dataSize:self.reviews.count]];
    CGFloat titleHeight = [self getTextHeight:review.user.name font:[UIFont boldSystemFontOfSize:FONT_SIZE]];
    CGFloat textHeight = [self getTextHeight:review.text font:[UIFont systemFontOfSize:FONT_SIZE]];
    return titleHeight + textHeight + (CELL_CONTENT_MARGIN * 2);
}

-(NSInteger)getIndexFrom:(NSInteger)infiniteScrollSectionIndex dataSize:(NSInteger)dataSize {
    return infiniteScrollSectionIndex % dataSize;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self.socialComments.layer removeAllAnimations];
    UIColor *background;    
    if (toInterfaceOrientation == UIInterfaceOrientationPortrait || toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) {
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
    } else {
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
    }
    self.view.backgroundColor = background;
    [self.socialComments reloadData];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self scrollComments];
}
     
- (CGFloat) getTextHeight:(NSString*) text font:(UIFont*) font{
    CGFloat CELL_CONTENT_WIDTH = self.socialComments.frame.size.width - (CELL_CONTENT_MARGIN*2);
    CGSize constraint = CGSizeMake(CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN * 2), 20000.0f);
    CGSize size = [text sizeWithFont:font constrainedToSize:constraint lineBreakMode:UILineBreakModeWordWrap];
    return size.height;
}

- (void)dealloc {
    [_socialComments release];
    [_textView release];
    [_rootController release];
    [_reviews release];
    [super dealloc];
}

- (IBAction)goHome:(id)sender {
    [self.timer invalidate];
    [self.rootController switchToController:@"SplashScreenController"];
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if (scrollView.contentOffset.y == scrollView.contentSize.height - scrollView.frame.size.height) {
        [CATransaction begin];
        [CATransaction disableActions];
        self.socialComments.contentOffset = CGPointMake(0, 0);
        [CATransaction commit];
    }
}

-(void) scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    [self.socialComments.layer removeAllAnimations];
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    commentsContentOffset = scrollView.contentOffset.y;
}

@end
