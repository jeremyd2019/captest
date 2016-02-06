#!/usr/bin/perl

use strict;
use warnings;

use Data::Dumper;
use DBI qw(:sql_types);
use JSON;
use List::Util qw(all);

use constant DEBUG => 0;

my $json = JSON->new->utf8;
$json = $json->pretty if &DEBUG;

my ($dbpath_a, $dbpath_b) = @ARGV[0..1];

my $dbh = DBI->connect("dbi:SQLite:dbname=$dbpath_a", "", "", {RaiseError=>1, AutoCommit=>1});

$dbh->do("ATTACH DATABASE '$dbpath_b' AS cms");
$dbh->{AutoCommit} = 0;
my @tbls = grep {$_!~/^(?:sqlite_)/} map { $_->[0] } @{$dbh->table_info(undef, "main", undef, "TABLE")->fetchall_arrayref([2])};

my %ret = ();

#print STDERR Dumper(\@tbls);

foreach my $table (@tbls)
{
	my @cols = map { $_->[0] } @{$dbh->column_info(undef, "main", $table, undef)->fetchall_arrayref([3])};
	my @keycols = $dbh->primary_key(undef, "main", $table);
	#print STDERR "table = $table, cols = '" . join(",",@cols) . "', keycols = '".join(",", @keycols)."'\n";
	# if we were using a REAL database, we could do a full outer join instead of two queries
	my $sql = "SELECT " . join(", ", map { "main.$table.$_" } @keycols). ", " . join(", ", map { "cms.$table.$_" } @cols) . " FROM cms.$table LEFT JOIN main.$table ON (" . join(" AND ", map { "cms.$table.$_ = main.$table.$_" } @keycols) . ") WHERE " . join(" OR ", map { "cms.$table.$_ <> main.$table.$_ OR ((cms.$table.$_ IS NULL OR main.$table.$_ IS NULL) AND NOT (cms.$table.$_ IS NULL AND main.$table.$_ IS NULL))" } @cols);
	#print STDERR $sql, "\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute;
	while (my $row = $sth->fetchrow_arrayref)
	{
		push @{$ret{$table}{(all { defined($_) } @$row[0..$#keycols]) ? "changed" : "added"}}, [@$row[scalar(@keycols)..$#$row]];
	}
	$sth->finish;
	$sql = "SELECT " . join(", ", map { "main.$table.$_" } @keycols) . " FROM main.$table LEFT JOIN cms.$table ON (" . join(" AND ", map { "main.$table.$_ = cms.$table.$_" } @keycols) . ") WHERE " . join(" AND ", map { "cms.$table.$_ IS NULL" } @keycols);
	#print STDERR $sql, "\n";
	$sth = $dbh->prepare($sql);
	$sth->execute;
	while (my $row = $sth->fetchrow_arrayref)
	{
		push @{$ret{$table}{"removed"}}, [@$row];
	}
	$sth->finish;
}

print $json->encode(\%ret);

$dbh->rollback;
$dbh->disconnect;

