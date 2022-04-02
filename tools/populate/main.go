package main

import (
	"database/sql"
	_ "embed"
	"flag"
	"fmt"
	"log"
	"math/rand"
	"strings"
	"time"

	_ "github.com/lib/pq"
)

//go:embed resources/prenom.txt
var rawprenom string

//go:embed resources/surnom.txt
var rawsurnom string

var db *sql.DB

var userGoal int
var campaignsPerUser int
var galleriesPerCampaign int

var userStmt *sql.Stmt
var campaignStmt *sql.Stmt
var galleryStmt *sql.Stmt

var prenom []string
var surnom []string

func addUser() {
	name := makeName()
	var userId int64

	digits := rand.Intn(9999)

	email := fmt.Sprintf("%s%d@mailinator.org", strings.Replace(name, " ", "_", 2), digits)
	rows, err := userStmt.Query(name, email)
	if err != nil {
		log.Fatalf("user insert: %v", err)
	}
	defer rows.Close()
	rows.Next()
	rows.Scan(&userId)

	addCampaign(userId)
}

func addCampaign(userId int64) {
	goal := rand.Intn(campaignsPerUser) + 1
	var campaignId int64
	for i := 0; i < goal; i++ {
		rows, err := campaignStmt.Query(userId, makeCampaignName())
		if err != nil {
			log.Fatalf("campaign insert: %v", err)
		}
		defer rows.Close()
		rows.Next()
		rows.Scan(&campaignId)
		addGallery(campaignId)
	}
}

func addGallery(campaignId int64) {
	goal := rand.Intn(galleriesPerCampaign) + 1
	for i := 0; i < goal; i++ {
		name := makeName()
		galleryname := fmt.Sprintf("%s.adv", strings.Replace(name, " ", "-", -1))
		content := fmt.Sprintf("rival %s: Adversary 1", name)
		_, err := galleryStmt.Exec(campaignId, galleryname, content)
		if err != nil {
			log.Fatalf("gallery insert: %v", err)
		}
	}
}

func main() {

	flag.IntVar(&userGoal, "users", 100, "Number of users to add")
	flag.IntVar(&campaignsPerUser, "campaigns", 4, "Max number of campaigns per user")
	flag.IntVar(&galleriesPerCampaign, "galleries", 6, "Max number of galleries per campaign")

	flag.Parse()

	fmt.Printf("Creating %d users, 1-%d campaigns per user, 1-%d galleries per campaign\n",
		userGoal, campaignsPerUser, galleriesPerCampaign)

	db, err := sql.Open("postgres", "postgres://blakeney:scarletpimpernil@localhost/blakeney?sslmode=disable")
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	rand.Seed(time.Now().UnixMilli())

	userStmt, err = db.Prepare("INSERT INTO appuser (name, email) VALUES ($1, $2) RETURNING id;")
	if err != nil {
		log.Fatalf("user statement: %v", err)
	}
	defer userStmt.Close()

	campaignStmt, err = db.Prepare("INSERT INTO campaign (user_id, name, active) VALUES ($1, $2, true) RETURNING id;")
	if err != nil {
		log.Fatalf("campaign statement: %v", err)
	}
	defer campaignStmt.Close()

	galleryStmt, err = db.Prepare("INSERT INTO gallery (campaign_id, name, content, active) VALUES ($1, $2, $3, true);")
	if err != nil {
		log.Fatalf("gallery statement: %v", err)
	}
	defer galleryStmt.Close()

	loadNames()

	for i := 0; i < userGoal; i++ {
		addUser()
		if ((i + 1) % 100) == 0 {
			log.Printf("%d users", i+1)
		}
	}

}

func loadNames() {
	prenom = strings.Split(rawprenom, "\n")
	surnom = strings.Split(rawsurnom, "\n")
}

func makeName() string {
	prenomlen := len(prenom)
	surnomlen := len(surnom)

	first := prenom[rand.Intn(prenomlen)]
	second := surnom[rand.Intn(surnomlen)]

	return fmt.Sprintf("%s %s", first, second)
}

func makeCampaignName() string {
	var ADJECTIVES = []string{"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer",
		"icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient",
		"twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing",
		"broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering",
		"bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small",
		"sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black",
		"young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral",
		"restless", "divine", "polished", "ancient", "purple", "lively", "nameless"}

	var NOUNS = []string{"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning",
		"snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest",
		"hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly",
		"bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass",
		"haze", "mountain", "night", "pond", "darkness", "snowflake", "silence",
		"sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower",
		"wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog",
		"frost", "voice", "paper", "frog", "smoke", "star"}

	adjLen := len(ADJECTIVES)
	nounLen := len(NOUNS)

	return fmt.Sprintf("%s-%s", ADJECTIVES[rand.Intn(adjLen)], NOUNS[rand.Intn(nounLen)])
}
