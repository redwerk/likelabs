#import <QuartzCore/QuartzCore.h>
#import "TextReviewController.h"
#import "SettingsDao.h"
#import "Review.h"
#import "User.h"
#import "RootMessageController.h"

static NSString *const GREETING = @"Start typing a message!";
static NSString *const TEXT_CELL_IDENTIFIER = @"textCellIdeintifier";
static NSString *const bgLandscape = @"textmessage_landscape.png";
static NSString *const bgPortrait = @"textmessage_portrait.png";

@interface TextReviewController()
@property (retain, nonatomic) UIViewController <ContainerController>* rootController;
@property (retain, nonatomic) NSArray* reviews;
@property (assign, nonatomic) Review* review;
@property (retain, nonatomic) NSTimer* timer;
@property (assign, nonatomic) BOOL textPlaceholderActive;
@property (assign, nonatomic) UIInterfaceOrientation currentOrientation;

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
@synthesize textPlaceholderActive = _textPlaceholderActive;
@synthesize btnNext = _btnNext;
@synthesize textLabel = _textLabel;
@synthesize review = _review;
@synthesize currentOrientation = _currentOrientation;


#pragma mark - Lifecycle

- (id)initWithRootController:(UIViewController <ContainerController> *)rootController {
    if (self = [super init]) {
        self.rootController = rootController;
        SettingsDao* dao = [[SettingsDao alloc] init];
        self.reviews = dao.textReviews;
        self.review = [self.rootController getReview];
        [dao release];
    }
    return self;
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    self.timer = [NSTimer scheduledTimerWithTimeInterval:ANIMATION_DURATION target:self selector:@selector(scrollComments) userInfo:nil repeats:YES];
    commentsContentOffset = self.socialComments.contentSize.height/2;
    [self scrollComments];
}

- (void)viewDidDisappear:(BOOL)animated {
    [self.timer invalidate];
    self.timer = nil;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self willAnimateRotationToInterfaceOrientation:[self interfaceOrientation] duration:0];
    // Do any additional setup after loading the view from its nib.
    [self.view setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    [self.socialComments setBackgroundView:nil];
    [self.socialComments setBackgroundView:[[[UIView alloc] init] autorelease]];
    [self.socialComments setBackgroundColor:[UIColor clearColor]];
    
    self.textView.layer.borderColor = [[UIColor colorWithWhite:BORDER_COLOR alpha:1.0] CGColor];
    self.textView.layer.borderWidth = BORDER_WIDTH;
    self.textView.layer.cornerRadius = BORDER_CORNER_RADIUS;
    if (self.review.text && self.review.text.length) {
        self.textView.text = self.review.text;
        self.textPlaceholderActive = false;
        self.textView.textColor = [UIColor blackColor];
    } else {
        self.textPlaceholderActive = true;
    }
    self.textLabel.font = [UIFont fontWithName:@"Lobster 1.4" size:23];    
    [self.textView becomeFirstResponder];
    self.currentOrientation = self.interfaceOrientation;
}

- (void)viewDidUnload
{
    [self setSocialComments:nil];
    [self setTextView:nil];
    [self setBtnNext:nil];
    [self setTextLabel:nil];
    [self setReview:nil];
    [super viewDidUnload];    
}

- (void)dealloc {
    self.rootController = nil;
    self.reviews = nil;
    [_socialComments release];
    [_textView release];
    [_btnNext release];
    [_textLabel release];
    [super dealloc];
}

#pragma mark - TextViewDelegate implementation
- (BOOL) resignFirstResponder {
    return [self.textView resignFirstResponder ];
}

- (BOOL)textView:(UITextView *)view shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (self.textPlaceholderActive) {
        self.textView.text = @"";
        self.textView.textColor = [UIColor blackColor];
        self.textPlaceholderActive = false;
    } 
    return YES;
}

- (void)textViewDidChange:(UITextView* ) view {
    if (self.textView.text.length == 0) {
        self.textView.textColor = [UIColor lightGrayColor];
        self.textView.text = GREETING;
        self.textPlaceholderActive = true;
    }
}

#pragma mark - TableViewDelegate implementation

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return (self.reviews.count > 0) ? ROWS_ENDLESS : 0;
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
        cell.opaque = NO;
        cell.clipsToBounds = NO;
        cell.backgroundView = [[[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"promo_bg_gray.png"] stretchableImageWithLeftCapWidth:0 topCapHeight:5]] autorelease];
        label = [[UILabel alloc] initWithFrame:CGRectZero];

        [label setLineBreakMode:UILineBreakModeWordWrap];
        [label setNumberOfLines:0];
        [label setFont:[UIFont fontWithName:@"BadScript-Regular" size:15]];
        [label setBackgroundColor:[UIColor clearColor]];
        [label setTag:1];
        titleLabel = [[UILabel alloc] initWithFrame:CGRectZero];

        [titleLabel setLineBreakMode:UILineBreakModeWordWrap];
        [titleLabel setMinimumFontSize:FONT_SIZE];
        [titleLabel setNumberOfLines:0];
        [titleLabel setFont:[UIFont boldSystemFontOfSize:FONT_SIZE]];
        [titleLabel setBackgroundColor:[UIColor clearColor]];
        [titleLabel setTag:2];
        titleLabel.textColor = [UIColor colorWithRed:25.0/255.0 green:137.0/255.0 blue:170.0/255.0 alpha:1.0];
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
    
    if (UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
        CELL_CONTENT_WIDTH = 250;
        [label setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN + titleHeight, 190, 200)];
        [titleLabel setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH*2), titleHeight)];
        cell.contentView.transform = CGAffineTransformMakeRotation(M_PI_2);
        
    } else {
        [label setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN + titleHeight, 300, MAX(textHeight, MIN_TEXT_HEIGHT))];
        [titleLabel setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH*2), titleHeight)];
        cell.contentView.transform = CGAffineTransformMakeRotation(0);
    }
    [label setText:review.text];
    [titleLabel setText:review.user.name];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (UIInterfaceOrientationIsPortrait([self interfaceOrientation])){
        return 209;
    }
    Review* review = [self.reviews objectAtIndex:[self getIndexFrom:indexPath.section dataSize:self.reviews.count]];
    CGFloat titleHeight = [self getTextHeight:review.user.name font:[UIFont boldSystemFontOfSize:FONT_SIZE]];
    CGFloat textHeight = [self getTextHeight:review.text font:[UIFont systemFontOfSize:FONT_SIZE]];
    return titleHeight + textHeight + (CELL_CONTENT_MARGIN * 4);
}

-(NSInteger)getIndexFrom:(NSInteger)infiniteScrollSectionIndex dataSize:(NSInteger)dataSize {
    return (dataSize) ? (infiniteScrollSectionIndex % dataSize) : -1;
}

- (CGFloat) getTextHeight:(NSString*) text font:(UIFont*) font{
    CGFloat CELL_CONTENT_WIDTH = self.socialComments.frame.size.width - (CELL_CONTENT_MARGIN*2);
    CGSize constraint = CGSizeMake(CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN * 2), 20000.0f);
    CGSize size = [text sizeWithFont:font constrainedToSize:constraint lineBreakMode:UILineBreakModeWordWrap];
    return size.height;
}

#pragma mark - Rotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    
    [self.socialComments.layer removeAllAnimations];

    UIColor *background;    
   // CGFloat oldOffset = self.socialComments.contentSize.height;
    CGFloat viewPadding = 20;
    if (UIInterfaceOrientationIsPortrait(toInterfaceOrientation)){
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
        if(!UIInterfaceOrientationIsPortrait(self.currentOrientation)){
            [self.socialComments setFrame: CGRectMake(0, 0, 250, 756)];
            [self.socialComments setTransform:CGAffineTransformMakeRotation(-M_PI_2)];
            
            self.socialComments.center = CGPointMake(390, self.socialComments.frame.size.height/2+162);
            self.textLabel.center = CGPointMake(384, 490);
            self.textView.frame = CGRectMake(33, 520, 700, 110);
            self.btnNext.frame = CGRectMake(33, 640, 700, 80);
            //self.socialComments.contentOffset = CGPointMake(0, self.socialComments.contentOffset.y * oldOffset/self.socialComments.contentSize.height);
        }
    } else {
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
        [self.socialComments setTransform:CGAffineTransformMakeRotation(0)];
        [self.socialComments setFrame: CGRectMake(0, 0, 340, 610)];
        self.socialComments.center = CGPointMake(self.socialComments.frame.size.width/2+viewPadding, self.socialComments.frame.size.height/2+153);

        self.textLabel.center = CGPointMake(720, 130);
        self.textView.frame = CGRectMake(453, 150, 515, 135);
        self.btnNext.frame = CGRectMake(453, 300, 515, 85);
        //self.socialComments.contentOffset = CGPointMake(0, self.socialComments.contentOffset.y * self.socialComments.contentSize.height/oldOffset);
    }
    self.view.backgroundColor = background;
    [self.socialComments reloadData];
    self.currentOrientation = toInterfaceOrientation;
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self scrollComments];
}

#pragma mark - Actions

- (IBAction)step:(id)sender {
    if (self.textPlaceholderActive) {
        return;
    }
    
    [[self.rootController getReview] setText:self.textView.text];
    [self.rootController step];
}

#pragma mark - Scrolling

-(void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if (scrollView.contentOffset.y == scrollView.contentSize.height - scrollView.frame.size.height) {
        [CATransaction begin];
        [CATransaction disableActions];
        self.socialComments.contentOffset = CGPointMake(0, 0);
        [CATransaction commit];
    }
    if(scrollView.contentOffset.y < scrollView.frame.size.height){
        [CATransaction begin];
        [CATransaction disableActions];
        self.socialComments.contentOffset = CGPointMake(0, scrollView.contentSize.height);
        [CATransaction commit];
    }
    
}

-(void) scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    [self.socialComments.layer removeAllAnimations];
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    commentsContentOffset = scrollView.contentOffset.y;
}

- (void) scrollComments {   
    commentsContentOffset -=ANIMATION_SPEED;
    [self.socialComments setContentOffset: CGPointMake(0, commentsContentOffset) animated:NO];
}

@end
