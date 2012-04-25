#import <QuartzCore/QuartzCore.h>
#import "TextReviewController.h"
#import "SettingsDao.h"
#import "Review.h"
#import "User.h"

static NSString *const GREETING = @"Start typing a message!";
static NSString *const TEXT_CELL_IDENTIFIER = @"textCellIdeintifier";
static NSString *const bgLandscape = @"bg_landscape.png";
static NSString *const bgPortrait = @"bg_portrait.png";


@interface TextReviewController()
- (CGFloat) getTextHeight:(NSString*) text font:(UIFont*) font;
@end

@implementation TextReviewController

const float BORDER_COLOR = 0.8f;
const float BORDER_WIDTH = 2.0f;
const float BORDER_CORNER_RADIUS = 10.0f;
const float FONT_SIZE = 14.0f;
const float CELL_CONTENT_MARGIN = 10.0f;
const float CELL_CONTENT_MARGIN_WIDTH = 10.0f;
const float MIN_TEXT_HEIGHT = 44.0f;

BOOL textPlaseholderActive = true;

@synthesize socialComments;
@synthesize textView;



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
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
    
    //[socialComments setBackgroundColor:[UIColor clearColor]];
    //[[socialComments backgroundView] setBackgroundColor:[UIColor clearColor]];
    [socialComments setBackgroundView:nil];
    [socialComments setBackgroundView:[[[UIView alloc] init] autorelease]];
    [socialComments setBackgroundColor:[UIColor clearColor]];
    
    
    UIColor *background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
    self.view.backgroundColor = background;

    textView.layer.borderColor = [[UIColor colorWithWhite:BORDER_COLOR alpha:1.0] CGColor];
    textView.layer.borderWidth = BORDER_WIDTH;
    textView.layer.cornerRadius = BORDER_CORNER_RADIUS;
    
    /*socialComments.layer.borderColor = [[UIColor colorWithWhite:BORDER_COLOR alpha:1.0] CGColor];
    socialComments.layer.borderWidth = BORDER_WIDTH;
    socialComments.layer.cornerRadius = BORDER_CORNER_RADIUS;*/
    
    [textView becomeFirstResponder];
}

- (void)viewDidUnload
{
    [self setSocialComments:nil];
    [self setTextView:nil];
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
    if (textPlaseholderActive) {
        textView.text = @"";
        textView.textColor = [UIColor blackColor];
        textPlaseholderActive = false;
    } 
    return YES;
}

- (void)textViewDidChange:(UITextView* ) view {
    if (textView.text.length == 0) {
        textView.textColor = [UIColor lightGrayColor];
        textView.text = GREETING;
        textPlaseholderActive = true;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    SettingsDao* dao = [[SettingsDao alloc] init];
    NSInteger count = (NSInteger)[dao getTextReviews].count;
    [dao release];
    return count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:TEXT_CELL_IDENTIFIER];
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
    }         
    if (!label) {
        label = (UILabel*)[cell viewWithTag:1];
    }
    if (!titleLabel) {
        titleLabel = (UILabel*)[cell viewWithTag:2];
    }
    
    SettingsDao* dao = [[SettingsDao alloc] init];
    Review* review = [[dao getTextReviews] objectAtIndex:indexPath.section];
    [dao release];    

    CGFloat textHeight = [self getTextHeight:review.text font:[UIFont systemFontOfSize:FONT_SIZE]];
    CGFloat titleHeight = [self getTextHeight:review.user.name font:[UIFont boldSystemFontOfSize:FONT_SIZE]];
    
    CGFloat CELL_CONTENT_WIDTH = socialComments.frame.size.width - (CELL_CONTENT_MARGIN_WIDTH*2);
    
    [label setText:review.text];
    [label setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN + titleHeight, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH * 2), MAX(textHeight, MIN_TEXT_HEIGHT))];
    
    [titleLabel setText:review.user.name];
    [titleLabel setFrame:CGRectMake(CELL_CONTENT_MARGIN_WIDTH, CELL_CONTENT_MARGIN, CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN_WIDTH*2), titleHeight)];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    SettingsDao* dao = [[SettingsDao alloc] init];
    Review* review = [[dao getTextReviews] objectAtIndex:indexPath.section];
    [dao release];
    CGFloat titleHeight = [self getTextHeight:review.user.name font:[UIFont boldSystemFontOfSize:FONT_SIZE]];
    CGFloat textHeight = [self getTextHeight:review.text font:[UIFont systemFontOfSize:FONT_SIZE]];
    return titleHeight + textHeight + (CELL_CONTENT_MARGIN * 2);    
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [socialComments reloadData];
    UIColor *background;
    if (toInterfaceOrientation == UIInterfaceOrientationPortrait || toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) {
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgPortrait]] autorelease];
    } else {
        background = [[[UIColor alloc] initWithPatternImage:[UIImage imageNamed:bgLandscape]] autorelease];
    }
    self.view.backgroundColor = background;
}
     
- (CGFloat) getTextHeight:(NSString*) text font:(UIFont*) font{
    CGFloat CELL_CONTENT_WIDTH = socialComments.frame.size.width - (CELL_CONTENT_MARGIN*2);
    CGSize constraint = CGSizeMake(CELL_CONTENT_WIDTH - (CELL_CONTENT_MARGIN * 2), 20000.0f);
    CGSize size = [text sizeWithFont:font constrainedToSize:constraint lineBreakMode:UILineBreakModeWordWrap];
    return size.height;
}

- (void)dealloc {
    [socialComments release];
    [textView release];
    [super dealloc];
}
@end
