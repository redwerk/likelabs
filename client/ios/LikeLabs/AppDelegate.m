#import "AppDelegate.h"
#import "TestFlight.h"
#import "SettingsDao.h"
#import "SettingsService.h"

@interface AppDelegate()

@property (nonatomic, retain) SettingsDao* settingsDao;

@end

@implementation AppDelegate

@synthesize window = _window;
@synthesize rootController = _rootController;
@synthesize settingsDao = _settingsDao;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]] autorelease];	

    [TestFlight takeOff:@"c64efb5c4a86d134fcc09ee1e7693304_NDY4MjgyMDExLTEyLTE0IDAxOjAwOjExLjUwMDcwMg"];
    _settingsDao = [[SettingsDao alloc] init];
    [self.settingsDao setUserDefaults];
   
    [self.window setRootViewController:self.rootController];
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}

- (void)applicationWillTerminate:(UIApplication *)application
{    
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:.
     */
    self.settingsDao.apiKey = @"";
    self.settingsDao.tabletId = NSUIntegerMax;
}

- (void)dealloc {
    [_settingsDao release];
    [_window release];
    [_rootController release];
    [super dealloc];
}
@end
